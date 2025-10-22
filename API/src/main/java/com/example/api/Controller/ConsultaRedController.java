package com.example.api.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.OrganizacionDTO;
import com.example.api.dto.PresidenteDTO;
import com.example.api.service.SoapClientService;
import com.example.api.soap.generated.OrganizationType;
import com.example.api.soap.generated.PresidentType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/consulta-red")
@Tag(name = "Red de ONGs (SOAP)", description = "Consulta información de presidentes y organizaciones de la red mediante servicio SOAP externo")
public class ConsultaRedController {

    @Autowired
    private SoapClientService soapClientService;

    @PostMapping("/consultar")
    @Operation(
        summary = "Consultar presidentes y organizaciones por IDs",
        description = "Obtiene información de presidentes y organizaciones de la red mediante servicio SOAP. " +
                      "Requiere una lista de IDs de organizaciones. El servicio retorna los datos de los presidentes " +
                      "y las organizaciones correspondientes a esos IDs."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "presidentes": [
                        {
                          "id": 52,
                          "name": "Juan Pérez",
                          "address": "Av. Principal 123",
                          "phone": "+34 600 00 052",
                          "organizationId": 6
                        }
                      ],
                      "organizaciones": [
                        {
                          "id": 6,
                          "name": "ONG Ejemplo",
                          "address": "Calle 28 # 128",
                          "phone": "+34 600 00 028"
                        }
                      ],
                      "totalPresidentes": 1,
                      "totalOrganizaciones": 1
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request inválido - lista de IDs vacía o faltante"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error al comunicarse con el servicio SOAP externo"
        )
    })
    public ResponseEntity<Map<String, Object>> consultarPresidentesYOrganizaciones(
            @Parameter(
                description = "Objeto JSON con array de IDs de organizaciones a consultar",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        name = "Ejemplo de request",
                        value = """
                        {
                          "orgIds": ["1", "2", "3"]
                        }
                        """
                    )
                )
            )
            @RequestBody Map<String, List<String>> request) {
        
        try {
            List<String> orgIds = request.get("orgIds");
            
            if (orgIds == null || orgIds.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Debe proporcionar al menos un ID de organización"));
            }

            // Llamar al servicio SOAP
            List<PresidentType> presidentes = soapClientService.obtenerPresidentes(orgIds);
            List<OrganizationType> organizaciones = soapClientService.obtenerOrganizaciones(orgIds);

            // Mapear a DTOs
            List<PresidenteDTO> presidentesDTO = mapPresidentesToDTO(presidentes);
            List<OrganizacionDTO> organizacionesDTO = mapOrganizationsToDTO(organizaciones);

            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("presidentes", presidentesDTO);
            response.put("organizaciones", organizacionesDTO);
            response.put("totalPresidentes", presidentesDTO.size());
            response.put("totalOrganizaciones", organizacionesDTO.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error en consultarPresidentesYOrganizaciones: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al consultar el servicio SOAP: " + e.getMessage()));
        }
    }

    private List<PresidenteDTO> mapPresidentesToDTO(List<PresidentType> presidentes) {
        List<PresidenteDTO> result = new ArrayList<>();
        
        for (PresidentType presidente : presidentes) {
            PresidenteDTO dto = new PresidenteDTO();
            
            // Extraer valores de JAXBElement
            if (presidente.getId() != null && presidente.getId().getValue() != null) {
                dto.setId(presidente.getId().getValue().intValue());
            }
            
            if (presidente.getName() != null && presidente.getName().getValue() != null) {
                dto.setName(presidente.getName().getValue());
            }
            
            if (presidente.getAddress() != null && presidente.getAddress().getValue() != null) {
                dto.setAddress(presidente.getAddress().getValue());
            }
            
            if (presidente.getPhone() != null && presidente.getPhone().getValue() != null) {
                dto.setPhone(presidente.getPhone().getValue());
            }
            
            if (presidente.getOrganizationId() != null && presidente.getOrganizationId().getValue() != null) {
                dto.setOrganizationId(presidente.getOrganizationId().getValue().intValue());
            }
            
            result.add(dto);
        }
        
        return result;
    }

    private List<OrganizacionDTO> mapOrganizationsToDTO(List<OrganizationType> organizaciones) {
        List<OrganizacionDTO> result = new ArrayList<>();
        
        for (OrganizationType org : organizaciones) {
            OrganizacionDTO dto = new OrganizacionDTO();
            
            // Extraer valores de JAXBElement
            if (org.getId() != null && org.getId().getValue() != null) {
                dto.setId(org.getId().getValue().intValue());
            }
            
            if (org.getName() != null && org.getName().getValue() != null) {
                dto.setName(org.getName().getValue());
            }
            
            if (org.getAddress() != null && org.getAddress().getValue() != null) {
                dto.setAddress(org.getAddress().getValue());
            }
            
            if (org.getPhone() != null && org.getPhone().getValue() != null) {
                dto.setPhone(org.getPhone().getValue());
            }
            
            result.add(dto);
        }
        
        return result;
    }
}