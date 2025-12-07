package com.example.myapplication.api.dto;

import java.util.UUID;

public class CategoryCreateDto {
    private String name;

    private UUID parentId;

    public CategoryCreateDto(String name, UUID parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public CategoryCreateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
