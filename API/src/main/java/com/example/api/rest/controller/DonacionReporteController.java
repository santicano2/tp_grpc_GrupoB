package com.example.api.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.rest.service.DonacionReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reportes de Donaciones", description = "Genera reportes agrupados por categoría en formato Excel")
@RestController
@RequestMapping("/reporte")
public class DonacionReporteController {

    private final DonacionReporteService reporteService;

    public DonacionReporteController(DonacionReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(
        summary = "Generar Excel de donaciones",
        description = "Genera un archivo Excel con todas las donaciones agrupadas por categoría."
    )
    @GetMapping(value = "/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generarExcel() {
        try {
            byte[] excelBytes = reporteService.generarExcelPorCategoria();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=donaciones.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
