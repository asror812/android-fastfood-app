package com.example.myapplication.api.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductResponseDto {
    private UUID id;

    private String name;

    private BigDecimal price;

    private int weight;

    private CategoryResponseDto category;

    private AttachmentResponseDto mainImage;

    private AttachmentResponseDto secondaryImage;

    private List<ProductDiscountResponseDto> productDiscounts;

    private List<BonusProductLinkResponseDTO> bonusProductLinks;

    public ProductResponseDto(UUID id, String name, BigDecimal price, int weight, CategoryResponseDto category, AttachmentResponseDto mainImage, AttachmentResponseDto secondaryImage, List<ProductDiscountResponseDto> productDiscounts, List<BonusProductLinkResponseDTO> bonusProductLinks) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.category = category;
        this.mainImage = mainImage;
        this.secondaryImage = secondaryImage;
        this.productDiscounts = productDiscounts;
        this.bonusProductLinks = bonusProductLinks;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public CategoryResponseDto getCategory() {
        return category;
    }

    public void setCategory(CategoryResponseDto category) {
        this.category = category;
    }

    public AttachmentResponseDto getMainImage() {
        return mainImage;
    }

    public void setMainImage(AttachmentResponseDto mainImage) {
        this.mainImage = mainImage;
    }

    public AttachmentResponseDto getSecondaryImage() {
        return secondaryImage;
    }

    public void setSecondaryImage(AttachmentResponseDto secondaryImage) {
        this.secondaryImage = secondaryImage;
    }

    public List<ProductDiscountResponseDto> getProductDiscounts() {
        return productDiscounts;
    }

    public void setProductDiscounts(List<ProductDiscountResponseDto> productDiscounts) {
        this.productDiscounts = productDiscounts;
    }

    public List<BonusProductLinkResponseDTO> getBonusProductLinks() {
        return bonusProductLinks;
    }

    public void setBonusProductLinks(List<BonusProductLinkResponseDTO> bonusProductLinks) {
        this.bonusProductLinks = bonusProductLinks;
    }

    @Override
    public String toString() {
        return "ProductResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
