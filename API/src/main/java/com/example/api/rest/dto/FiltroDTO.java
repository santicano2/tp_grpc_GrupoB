package com.example.api.rest.dto;

public class FiltroDTO {
    private Long id;
    private String nombreFiltro;
    private String usuario;
    private String criteriosJson; // guardamos los filtros como JSON

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreFiltro() { return nombreFiltro; }
    public void setNombreFiltro(String nombreFiltro) { this.nombreFiltro = nombreFiltro; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getCriteriosJson() { return criteriosJson; }
    public void setCriteriosJson(String criteriosJson) { this.criteriosJson = criteriosJson; }
}
