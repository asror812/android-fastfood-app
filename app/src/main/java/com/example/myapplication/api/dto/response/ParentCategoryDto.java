package com.example.myapplication.api.dto.response;

import java.util.UUID;

public class ParentCategoryDto {
    private UUID id;
    private String name;

    public ParentCategoryDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public ParentCategoryDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
