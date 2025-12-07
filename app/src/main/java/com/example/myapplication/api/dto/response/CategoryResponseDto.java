package com.example.myapplication.api.dto.response;

import java.util.List;
import java.util.UUID;

public class CategoryResponseDto {
    private UUID id;

    private String name;

    private ParentCategoryDto parent;

    private List<SubCategory> subCategories;

    public CategoryResponseDto(UUID id, String name, ParentCategoryDto parent, List<SubCategory> subCategories) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.subCategories = subCategories;
    }

    public CategoryResponseDto() {
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

    public ParentCategoryDto getParent() {
        return parent;
    }

    public void setParent(ParentCategoryDto parent) {
        this.parent = parent;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
