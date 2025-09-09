package com.example.api.dto;

public class EventDTO {
    private int id;
    private String nombre;
    private String descripcion;
    private String fechaEvento;
    private int usuarioCreacion;
    private int modificadoPor;
    private String fechaModificacion;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }

    public int getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(int usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    public int getModificadoPor() { return modificadoPor; }
    public void setModificadoPor(int modificadoPor) { this.modificadoPor = modificadoPor; }

    public String getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(String fechaModificacion) { this.fechaModificacion = fechaModificacion; }
}
