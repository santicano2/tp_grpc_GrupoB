package com.example.api.rest.service;

import com.example.api.rest.dto.DonacionReporteDTO;
import com.example.api.service.DonacionClientService;
import ong.inventory.Inventory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DonacionReporteService {

    private final DonacionClientService donacionClient;

    public DonacionReporteService(DonacionClientService donacionClient) {
        this.donacionClient = donacionClient;
    }

    public List<DonacionReporteDTO> obtenerDonaciones() {
        Inventory.DonationList donationList = donacionClient.listarDonaciones();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return donationList.getItemsList().stream().map(d -> {
            DonacionReporteDTO dto = new DonacionReporteDTO();
            dto.setCategoria(d.getCategory().name());
            dto.setDescripcion(d.getDescription());
            dto.setCantidad(d.getQuantity());
            dto.setEliminado(d.getDeleted());
            dto.setUsuarioAlta(d.getCreatedBy());
            dto.setUsuarioModificacion(d.getUpdatedBy());
            dto.setFechaAlta(d.getCreatedAt().isEmpty() ? null : LocalDateTime.parse(d.getCreatedAt(), formatter));
            return dto;
        }).collect(Collectors.toList());
    }

    public byte[] generarExcelPorCategoria() throws IOException {
        List<DonacionReporteDTO> donaciones = obtenerDonaciones();

        Workbook workbook = new XSSFWorkbook();
        Map<String, List<DonacionReporteDTO>> porCategoria = donaciones.stream()
                .collect(Collectors.groupingBy(DonacionReporteDTO::getCategoria));

        for (String categoria : porCategoria.keySet()) {
            Sheet sheet = workbook.createSheet(categoria);
            Row header = sheet.createRow(0);
            String[] columnas = {"Fecha de Alta","Descripcion","Cantidad","Eliminado","Usuario Alta","Usuario Modificacion"};
            for(int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            List<DonacionReporteDTO> registros = porCategoria.get(categoria);
            for (int i = 0; i < registros.size(); i++) {
                DonacionReporteDTO d = registros.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(d.getFechaAlta() != null ? d.getFechaAlta().toString() : "");
                row.createCell(1).setCellValue(d.getDescripcion());
                row.createCell(2).setCellValue(d.getCantidad());
                row.createCell(3).setCellValue(d.isEliminado());
                row.createCell(4).setCellValue(d.getUsuarioAlta());
                row.createCell(5).setCellValue(d.getUsuarioModificacion());
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream.toByteArray();
    }
}
