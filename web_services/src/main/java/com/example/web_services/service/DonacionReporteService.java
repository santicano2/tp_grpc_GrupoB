package com.example.web_services.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.web_services.dto.DonacionItemDTO;
import com.example.web_services.dto.DonacionReporteDTO; 
@Service
public class DonacionReporteService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // Inyecta la URL de la API desde application.properties
    @Value("${api.service.url}")
    private String apiUrl;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 

    public DonacionReporteService() {}

    public List<DonacionReporteDTO> obtenerDonaciones() {

        final String url = apiUrl + "/donaciones/listar"; 
        DonacionItemDTO[] donacionesRecibidas = restTemplate.getForObject(url, DonacionItemDTO[].class);
        
        if (donacionesRecibidas == null){
            return List.of();
        }
        return Arrays.stream(donacionesRecibidas)
                .map(this::mapToReporteDTO)
                .collect(Collectors.toList());
    }


    public byte[] generarExcelPorCategoria(String category, Boolean deleted, String dateFrom, String dateTo) throws IOException {
        List<DonacionReporteDTO> donaciones = obtenerDonaciones();
        
        // aplicar filtros
        donaciones = aplicarFiltros(donaciones, category, deleted, dateFrom, dateTo);
        
        Workbook workbook = new XSSFWorkbook();

        Map<String, List<DonacionReporteDTO>> porCategoria = donaciones.stream()
                .collect(Collectors.groupingBy(DonacionReporteDTO::getCategoria));

        for (String categoria : porCategoria.keySet()) {
            Sheet sheet = workbook.createSheet(categoria);
            Row header = sheet.createRow(0);
            String[] columnas = {"Fecha de Alta", "Descripcion", "Cantidad", "Eliminado", "Usuario Alta", "Usuario Modificacion"};
            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            List<DonacionReporteDTO> registros = porCategoria.get(categoria);
            for (int i = 0; i < registros.size(); i++) {
                DonacionReporteDTO d = registros.get(i);
                Row row = sheet.createRow(i + 1);
                // Formatea la fecha (LocalDateTime) a String para el Excel
                String fechaFormateada = "";
                if (d.getFechaAlta() != null) {
                    fechaFormateada = d.getFechaAlta().format(FORMATTER);
                }
                row.createCell(0).setCellValue(fechaFormateada);
                row.createCell(1).setCellValue(d.getDescripcion());
                row.createCell(2).setCellValue(d.getCantidad());
                row.createCell(3).setCellValue(d.isEliminado() ? "Sí" : "No");
                row.createCell(4).setCellValue(d.getUsuarioAlta());
                row.createCell(5).setCellValue(d.getUsuarioModificacion() != null ? d.getUsuarioModificacion() : "");
            }

            for (int j = 0; j < columnas.length; j++) {
                sheet.autoSizeColumn(j);
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream.toByteArray();
    }

    private List<DonacionReporteDTO> aplicarFiltros(List<DonacionReporteDTO> donaciones, 
                                                    String category, Boolean deleted, 
                                                    String dateFrom, String dateTo) {
        return donaciones.stream()
                .filter(d -> category == null || d.getCategoria().equalsIgnoreCase(category))
                .filter(d -> deleted == null || d.isEliminado() == deleted)
                .filter(d -> {
                    if (dateFrom == null || dateFrom.isEmpty()) return true;
                    try {
                        LocalDate from = LocalDate.parse(dateFrom, DATE_FORMATTER);
                        return d.getFechaAlta() != null && 
                            !d.getFechaAlta().toLocalDate().isBefore(from);
                    } catch (Exception e) {
                        return true;
                    }
                })
                .filter(d -> {
                    if (dateTo == null || dateTo.isEmpty()) return true;
                    try {
                        LocalDate to = LocalDate.parse(dateTo, DATE_FORMATTER);
                        return d.getFechaAlta() != null && 
                            !d.getFechaAlta().toLocalDate().isAfter(to);
                    } catch (Exception e) {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }

    // Mapeo
    private DonacionReporteDTO mapToReporteDTO(DonacionItemDTO item) {
        DonacionReporteDTO dto = new DonacionReporteDTO();
        dto.setCategoria(item.getCategory());
        dto.setDescripcion(item.getDescription());
        dto.setCantidad(item.getQuantity());
        dto.setEliminado(item.isDeleted());
        dto.setUsuarioAlta(item.getCreatedBy());
        dto.setUsuarioModificacion(item.getUpdatedBy());

        // Parsea el String de la fecha a un objeto LocalDateTime
        if (item.getCreatedAt() != null && !item.getCreatedAt().isEmpty()) {
            try {
                dto.setFechaAlta(LocalDateTime.parse(item.getCreatedAt(), ISO_FORMATTER));
            } catch (DateTimeParseException e) {
                System.err.println("Error al parsear fecha: " + item.getCreatedAt());
                dto.setFechaAlta(null);
            }
        }
        return dto;
    }
}