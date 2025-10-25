package com.example.web_services.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "filtros_guardados")
public class FiltroGuardado {

    public enum TipoFiltro {
        DONACIONES,
        EVENTOS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING) 
    @Column(name = "tipo", nullable = false)
    private TipoFiltro tipo;

    @Column(name = "filtros_json", nullable = false, columnDefinition = "TEXT")
    private String filtrosJson;

    @CreationTimestamp // JPA asignará la fecha de creación automáticamente
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Instant fechaCreacion;

    @UpdateTimestamp // JPA asignará la fecha de modificación automáticamente
    @Column(name = "fecha_modificacion", nullable = false)
    private Instant fechaModificacion;

    // --- Constructores ---

    public FiltroGuardado() {
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoFiltro getTipo() {
        return tipo;
    }

    public void setTipo(TipoFiltro tipo) {
        this.tipo = tipo;
    }

    public String getFiltrosJson() {
        return filtrosJson;
    }

    public void setFiltrosJson(String filtrosJson) {
        this.filtrosJson = filtrosJson;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}