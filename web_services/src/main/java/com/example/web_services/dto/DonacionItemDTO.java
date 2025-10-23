package com.example.web_services.dto;

import lombok.Data;


//DTO para RECIBIR los datos desde el endpoint /donaciones/listar de la API.

@Data // Genera getters, setters, toString, etc.
public class DonacionItemDTO {

    private int id;
    private String category;
    private String description;
    private int quantity;
    private boolean deleted;
    private String createdAt; 
    private String createdBy;
    private String updatedAt;
    private String updatedBy;

}