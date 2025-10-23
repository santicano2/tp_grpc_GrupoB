package com.example.web_services.dto;

import java.time.LocalDateTime;

import lombok.Data;


@Data 
public class DonacionReporteDTO {

    private String categoria;
    private String descripcion;
    private int cantidad;
    private boolean eliminado;
    private LocalDateTime fechaAlta; //El service convierte el String a LocalDateTime
    private String usuarioAlta;
    private String usuarioModificacion;


}