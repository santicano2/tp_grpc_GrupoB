package com.example.api.dto;

import java.util.List;

public class EventDTO {
    private int id;
    private String name;
    private String description;
    private String whenIso;
    private List<String> members;
    private String createdBy;
    private String modifiedBy;
    private String modificationDate;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWhenIso() { return whenIso; }
    public void setWhenIso(String whenIso) { this.whenIso = whenIso; }

    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

    public String getModificationDate() { return modificationDate; }
    public void setModificationDate(String modificationDate) { this.modificationDate = modificationDate; }
}
