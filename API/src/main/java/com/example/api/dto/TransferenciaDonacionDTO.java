package com.example.api.dto;

import java.util.List;

public class TransferenciaDonacionDTO {
    private Long idSolicitud;
    private Long idOrganizacionSolicitante;
    private Long idOrganizacionDonante;
    private List<DonacionItem> donaciones;

    public static class DonacionItem {
        private String categoria;
        private String descripcion;
        private String cantidad;

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public String getCantidad() { return cantidad; }
        public void setCantidad(String cantidad) { this.cantidad = cantidad; }
    }

    public Long getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Long idSolicitud) { this.idSolicitud = idSolicitud; }
    public Long getIdOrganizacionSolicitante() { return idOrganizacionSolicitante; }
    public void setIdOrganizacionSolicitante(Long idOrganizacionSolicitante) { this.idOrganizacionSolicitante = idOrganizacionSolicitante; }
    public Long getIdOrganizacionDonante() { return idOrganizacionDonante; }
    public void setIdOrganizacionDonante(Long idOrganizacionDonante) { this.idOrganizacionDonante = idOrganizacionDonante; }
    public List<DonacionItem> getDonaciones() { return donaciones; }
    public void setDonaciones(List<DonacionItem> donaciones) { this.donaciones = donaciones; }
}
