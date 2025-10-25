package com.example.web_services.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web_services.dto.FiltroDTO;
import com.example.web_services.service.FiltroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/filtros")
@Tag(name = "Filtros de Eventos", description = "Endpoints para gestionar filtros personalizados de eventos")
public class FiltroController {

    private final FiltroService service;

    public FiltroController(FiltroService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "Listar filtros guardados",
        description = "Obtiene todos los filtros personalizados guardados por el usuario para el informe de eventos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de filtros obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = FiltroDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<FiltroDTO>> listarFiltros(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(in = ParameterIn.HEADER, name = "X-Test-User-ID", 
                description = "PARA PRUEBAS: Busca los filtros del usuario por su ID.")
        String testUserIdHeader) {
        try {
            Long usuarioId = Long.parseLong(userDetails.getUsername());
            List<FiltroDTO> filtros = service.listarFiltros(usuarioId);
            return ResponseEntity.ok(filtros);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @Operation(
        summary = "Guardar nuevo filtro",
        description = "Guarda un nuevo filtro personalizado con los criterios de búsqueda especificados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Filtro guardado exitosamente",
            content = @Content(schema = @Schema(implementation = FiltroDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos del filtro inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<FiltroDTO> guardarFiltro(
        @Parameter(description = "Datos del filtro a guardar", required = true)
        @RequestBody FiltroDTO filtro,
        @AuthenticationPrincipal UserDetails userDetails,
        @Parameter(in = ParameterIn.HEADER, name = "X-Test-User-ID", 
                description = "PARA PRUEBAS: Busca los filtros del usuario por su ID.")
        String testUserIdHeader) {
        try {
            Long usuarioId = Long.parseLong(userDetails.getUsername());
            FiltroDTO filtroGuardado = service.guardarFiltro(filtro, usuarioId);

            return ResponseEntity.status(HttpStatus.CREATED).body(filtroGuardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    @Operation(
        summary = "Actualizar filtro existente",
        description = "Actualiza los criterios de un filtro personalizado ya guardado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filtro actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = FiltroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Filtro no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos del filtro inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<FiltroDTO> actualizarFiltro(
        @Parameter(description = "Datos actualizados del filtro", required = true)
        @RequestBody FiltroDTO filtro,
        @AuthenticationPrincipal UserDetails userDetails,
        @Parameter(in = ParameterIn.HEADER, name = "X-Test-User-ID", 
                description = "PARA PRUEBAS: Busca los filtros del usuario por su ID.")
        String testUserIdHeader) {
        try {
            Long usuarioId = Long.parseLong(userDetails.getUsername());
            FiltroDTO filtroActualizado = service.actualizarFiltro(filtro, usuarioId);
            
            if (filtroActualizado == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(filtroActualizado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar filtro",
        description = "Elimina un filtro personalizado guardado por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Filtro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Filtro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarFiltro(@PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        @Parameter(in = ParameterIn.HEADER, name = "X-Test-User-ID", 
                description = "PARA PRUEBAS: Busca los filtros del usuario por su ID.")
        String testUserIdHeader) {
        try {
            Long usuarioId = Long.parseLong(userDetails.getUsername());
            boolean result = service.eliminarFiltro(id, usuarioId);
            if (result) {
                // Se eliminó con éxito
                return ResponseEntity.ok().build(); 
            } else {
                return ResponseEntity.notFound().build(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
