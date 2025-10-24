package com.example.web_services.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web_services.service.DonacionReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reportes de Donaciones", description = "Genera reportes de donaciones en formato Excel con filtros opcionales")
@RestController
@RequestMapping("/reporte")
public class DonacionReporteController {

    private final DonacionReporteService reporteService;

    public DonacionReporteController(DonacionReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(
        summary = "Generar Excel de donaciones con filtros",
        description = "Genera un archivo Excel con las donaciones agrupadas por categoría en hojas separadas. " +
                    "Cada hoja contiene el detalle de las donaciones (Fecha de Alta, Descripción, Cantidad, Eliminado, Usuario Alta, Usuario Modificación). " +
                    "Todos los filtros son opcionales y se pueden combinar."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Excel generado exitosamente",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        ),
        @ApiResponse(responseCode = "500", description = "Error al generar el Excel")
    })
    @GetMapping(value = "/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> generarExcel(
            @Parameter(
                description = "Filtrar por categoría específica. Si no se especifica, incluye todas las categorías.",
                example = "ROPA",
                schema = @io.swagger.v3.oas.annotations.media.Schema(
                    allowableValues = {"ROPA", "ALIMENTOS", "JUGUETES", "UTILES_ESCOLARES"}
                )
            )
            @RequestParam(required = false) String category,
            
            @Parameter(
                description = "Filtrar por estado de eliminado. true = solo eliminadas, false = solo activas, null = todas",
                example = "false"
            )
            @RequestParam(required = false) Boolean deleted,
            
            @Parameter(
                description = "Fecha desde para filtrar por fecha de alta (formato ISO 8601: yyyy-MM-dd)",
                example = "2025-01-01"
            )
            @RequestParam(required = false) String dateFrom,
            
            @Parameter(
                description = "Fecha hasta para filtrar por fecha de alta (formato ISO 8601: yyyy-MM-dd)",
                example = "2025-12-31"
            )
            @RequestParam(required = false) String dateTo
    ) {
        try {
            byte[] excelBytes = reporteService.generarExcelPorCategoria(category, deleted, dateFrom, dateTo);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=donaciones.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
