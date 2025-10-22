package com.example.api.dto;

public class PresidenteDTO {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Integer organizationId;

    // Constructor vacío
    public PresidenteDTO() {}

    // Constructor completo
    public PresidenteDTO(Integer id, String name, String address, String phone, Integer organizationId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.organizationId = organizationId;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
}