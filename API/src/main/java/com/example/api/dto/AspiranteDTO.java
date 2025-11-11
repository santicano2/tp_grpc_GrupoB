package com.example.api.dto;

public class AspiranteDTO {
    private int id;
    private String nombre;
    private String apellido;
    private Integer edad;
    private String telefono;
    private String email;
    private String motivo;
    private String estado;
    private String motivoRechazo;
    private String fechaSolicitud;
    private String fechaProcesamiento;
    private String procesadoPor;

    // Constructor vacío
    public AspiranteDTO() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    public void setFechaProcesamiento(String fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public String getProcesadoPor() {
        return procesadoPor;
    }

    public void setProcesadoPor(String procesadoPor) {
        this.procesadoPor = procesadoPor;
    }

    @Override
    public String toString() {
        return "AspiranteDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", motivo='" + motivo + '\'' +
                ", estado='" + estado + '\'' +
                ", motivoRechazo='" + motivoRechazo + '\'' +
                ", fechaSolicitud='" + fechaSolicitud + '\'' +
                ", fechaProcesamiento='" + fechaProcesamiento + '\'' +
                ", procesadoPor='" + procesadoPor + '\'' +
                '}';
    }
}
