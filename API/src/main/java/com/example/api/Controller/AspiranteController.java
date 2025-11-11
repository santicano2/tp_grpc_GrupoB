package com.example.api.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.AspiranteDTO;
import com.example.api.service.AspiranteClientService;

import ong.aspirantes.Aspirantes;

@RestController
@RequestMapping("/aspirantes")
public class AspiranteController {

    private final AspiranteClientService aspiranteClientService;

    public AspiranteController(AspiranteClientService aspiranteClientService) {
        this.aspiranteClientService = aspiranteClientService;
    }

    /**
     * Endpoint público para crear solicitud de aspirante
     * No requiere autenticación - es el formulario "Quiero sumarme"
     */
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitar(@RequestBody AspiranteDTO aspiranteDTO) {
        try {
            // Validaciones básicas
            if (aspiranteDTO.getNombre() == null || aspiranteDTO.getNombre().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre es obligatorio"));
            }
            if (aspiranteDTO.getApellido() == null || aspiranteDTO.getApellido().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El apellido es obligatorio"));
            }
            if (aspiranteDTO.getEmail() == null || aspiranteDTO.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El email es obligatorio"));
            }
            if (aspiranteDTO.getMotivo() == null || aspiranteDTO.getMotivo().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El motivo es obligatorio"));
            }

            Aspirantes.Aspirante aspirante = aspiranteClientService.crearAspirante(
                aspiranteDTO.getNombre(),
                aspiranteDTO.getApellido(),
                aspiranteDTO.getEdad(),
                aspiranteDTO.getTelefono(),
                aspiranteDTO.getEmail(),
                aspiranteDTO.getMotivo()
            );

            return ResponseEntity.ok(Map.of(
                "message", "Solicitud enviada correctamente",
                "aspirante", mapToDTO(aspirante)
            ));

        } catch (io.grpc.StatusRuntimeException e) {
            String errorMessage = e.getStatus().getDescription();
            if (errorMessage != null && errorMessage.contains("email ya está registrado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El email ya está registrado como usuario"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", errorMessage != null ? errorMessage : "Error al procesar la solicitud"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Listar aspirantes pendientes (solo PRESIDENTE)
     */
    @GetMapping("/pendientes")
    public ResponseEntity<?> listarPendientes() {
        try {
            Aspirantes.AspiranteList list = aspiranteClientService.listarAspirantesPendientes();
            List<AspiranteDTO> dtos = new ArrayList<>();
            
            for (Aspirantes.Aspirante aspirante : list.getAspirantesList()) {
                dtos.add(mapToDTO(aspirante));
            }
            
            return ResponseEntity.ok(dtos);
            
        } catch (Exception e) {
            System.err.println("Error al obtener aspirantes pendientes: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener aspirantes"));
        }
    }

    /**
     * Obtener un aspirante por ID (solo PRESIDENTE)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable int id, @RequestParam String actor) {
        try {
            Aspirantes.Aspirante aspirante = aspiranteClientService.obtenerAspirante(id, actor);
            return ResponseEntity.ok(mapToDTO(aspirante));
            
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aspirante no encontrado"));
            }
            if (e.getStatus().getCode() == io.grpc.Status.Code.PERMISSION_DENIED) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "No autorizado"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getStatus().getDescription()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Rechazar aspirante (solo PRESIDENTE)
     */
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable int id, 
                                     @RequestParam String actor,
                                     @RequestBody Map<String, String> body) {
        try {
            String motivoRechazo = body.get("motivoRechazo");
            
            if (motivoRechazo == null || motivoRechazo.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El motivo de rechazo es obligatorio"));
            }

            Aspirantes.Aspirante aspirante = aspiranteClientService.rechazarAspirante(id, motivoRechazo, actor);
            
            return ResponseEntity.ok(Map.of(
                "message", "Aspirante rechazado correctamente",
                "aspirante", mapToDTO(aspirante)
            ));
            
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.PERMISSION_DENIED) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "No autorizado"));
            }
            if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aspirante no encontrado"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getStatus().getDescription()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Aceptar aspirante (solo PRESIDENTE)
     * Marca el aspirante como ACEPTADO para rellenar el formulario de alta de usuario
     */
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptar(@PathVariable int id, @RequestParam String actor) {
        try {
            Aspirantes.Aspirante aspirante = aspiranteClientService.aceptarAspirante(id, actor);
            
            return ResponseEntity.ok(Map.of(
                "message", "Aspirante aceptado correctamente",
                "aspirante", mapToDTO(aspirante)
            ));
            
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.PERMISSION_DENIED) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "No autorizado"));
            }
            if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aspirante no encontrado"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getStatus().getDescription()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Mapear mensaje gRPC a DTO
     */
    private AspiranteDTO mapToDTO(Aspirantes.Aspirante aspirante) {
        AspiranteDTO dto = new AspiranteDTO();
        dto.setId(aspirante.getId());
        dto.setNombre(aspirante.getNombre());
        dto.setApellido(aspirante.getApellido());
        dto.setEdad(aspirante.getEdad() > 0 ? aspirante.getEdad() : null);
        dto.setTelefono(aspirante.getTelefono());
        dto.setEmail(aspirante.getEmail());
        dto.setMotivo(aspirante.getMotivo());
        dto.setEstado(aspirante.getEstado().name());
        dto.setMotivoRechazo(aspirante.getMotivoRechazo());
        dto.setFechaSolicitud(aspirante.getFechaSolicitud());
        dto.setFechaProcesamiento(aspirante.getFechaProcesamiento());
        dto.setProcesadoPor(aspirante.getProcesadoPor());
        return dto;
    }
}
