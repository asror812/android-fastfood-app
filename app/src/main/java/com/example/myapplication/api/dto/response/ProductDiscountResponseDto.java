package com.example.myapplication.api.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class ProductDiscountResponseDto {
    private UUID id;
    private UUID productId;

    private UUID discountId;
    private String name;
    private int percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private int requiredQuantity;

    private boolean isActive;

    public ProductDiscountResponseDto(UUID id, UUID productId, UUID discountId, String name, int percentage, LocalDate startDate, LocalDate endDate, int requiredQuantity, boolean isActive) {
        this.id = id;
        this.productId = productId;
        this.discountId = discountId;
        this.name = name;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requiredQuantity = requiredQuantity;
        this.isActive = isActive;
    }

    public ProductDiscountResponseDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getDiscountId() {
        return discountId;
    }

    public void setDiscountId(UUID discountId) {
        this.discountId = discountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
