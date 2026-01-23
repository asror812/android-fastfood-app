package com.example.myapplication.api.dto.response.product;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProductResponseDto {
    private UUID id;
    private String name;
    private BigDecimal price;
    private CategoryResponseDto category;
    private int weight;
    private List<ProductDiscountResponseDto> discounts = new ArrayList<>();
    private List<ProductBonusDto> bonuses = new ArrayList<>();
    private List<ProductImageResponseDto> images = new ArrayList<>();

    private boolean favorite;


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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    public List<ProductBonusDto> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<ProductBonusDto> bonuses) {
        this.bonuses = bonuses;
    }

    public List<ProductImageResponseDto> getImages() {
        return images;
    }

    public void setImages(List<ProductImageResponseDto> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ProductResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public List<ProductDiscountResponseDto> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<ProductDiscountResponseDto> discounts) {
        this.discounts = discounts;
    }
}
