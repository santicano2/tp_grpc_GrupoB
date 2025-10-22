package com.example.api.rest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.rest.dto.FiltroDTO;
import com.example.api.rest.service.FiltroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
            content = @Content(schema = @Schema(implementation = FiltroDTO.class)))
    })
    public List<FiltroDTO> listarFiltros() {
        return service.listarFiltros();
    }

    @PostMapping
    @Operation(
        summary = "Guardar nuevo filtro",
        description = "Guarda un nuevo filtro personalizado con los criterios de búsqueda especificados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filtro guardado exitosamente",
            content = @Content(schema = @Schema(implementation = FiltroDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos del filtro inválidos")
    })
    public FiltroDTO guardarFiltro(
        @Parameter(description = "Datos del filtro a guardar", required = true)
        @RequestBody FiltroDTO filtro) {
        return service.guardarFiltro(filtro);
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
        @ApiResponse(responseCode = "400", description = "Datos del filtro inválidos")
    })
    public FiltroDTO actualizarFiltro(
        @Parameter(description = "Datos actualizados del filtro", required = true)
        @RequestBody FiltroDTO filtro) {
        return service.actualizarFiltro(filtro);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar filtro",
        description = "Elimina un filtro personalizado guardado por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filtro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Filtro no encontrado")
    })
    public ResponseEntity<Boolean> eliminarFiltro(
        @Parameter(description = "ID del filtro a eliminar", required = true, example = "1")
        @PathVariable Long id) {
        boolean result = service.eliminarFiltro(id);
        return ResponseEntity.ok(result);
    }
}
