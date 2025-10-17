package com.example.api.rest.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.swagger.v3.oas.annotations.media.Schema;
import ong.inventory.Inventory;

@Schema(description = "Representa una donación para el reporte en Excel")
public class DonacionReporteDTO {

    @Schema(description = "Categoría de la donación", example = "ROPA")
    private String categoria;

    @Schema(description = "Descripción de la donación", example = "Camperas de abrigo")
    private String descripcion;

    @Schema(description = "Cantidad donada", example = "15")
    private int cantidad;

    @Schema(description = "Indica si la donación fue eliminada", example = "false")
    private boolean eliminado;

    @Schema(description = "Fecha de creación de la donación", example = "2025-10-16T12:00:00")
    private LocalDateTime fechaAlta;

    @Schema(description = "Usuario que creó la donación", example = "juanperez")
    private String usuarioAlta;

    @Schema(description = "Usuario que modificó la donación por última vez", example = "maria123")
    private String usuarioModificacion;

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getUsuarioAlta() { return usuarioAlta; }
    public void setUsuarioAlta(String usuarioAlta) { this.usuarioAlta = usuarioAlta; }

    public String getUsuarioModificacion() { return usuarioModificacion; }
    public void setUsuarioModificacion(String usuarioModificacion) { this.usuarioModificacion = usuarioModificacion; }

    // --- Método estático agregado ---
    public static DonacionReporteDTO fromDonationItem(Inventory.DonationItem d) {
        DonacionReporteDTO dto = new DonacionReporteDTO();
        dto.setCategoria(d.getCategory().name());
        dto.setDescripcion(d.getDescription());
        dto.setCantidad(d.getQuantity());
        dto.setEliminado(d.getDeleted());
        dto.setUsuarioAlta(d.getCreatedBy());
        dto.setUsuarioModificacion(d.getUpdatedBy());

        if (!d.getCreatedAt().isEmpty()) {
            try {
                dto.setFechaAlta(LocalDateTime.parse(d.getCreatedAt(),
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (Exception e) {
                System.err.println("Error parseando fecha: " + d.getCreatedAt());
            }
        }
        return dto;
    }
}
