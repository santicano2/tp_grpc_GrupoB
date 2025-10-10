package com.example.api.dto;

public class AdhesionEventoDTO {
    
    private Long idEvento;
    private Long idOrganizacion;
    private Long idVoluntario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    public Long getIdEvento() { return idEvento; }
    public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }

    public Long getIdOrganizacion() { return idOrganizacion; }
    public void setIdOrganizacion(Long idOrganizacion) { this.idOrganizacion = idOrganizacion; }

    public Long getIdVoluntario() { return idVoluntario; }
    public void setIdVoluntario(Long idVoluntario) { this.idVoluntario = idVoluntario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
