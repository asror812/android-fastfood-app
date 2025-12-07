package com.example.myapplication.api.dto;

import java.util.UUID;

public class BonusProductLinkDto {
    private UUID bonusId;
    private UUID productId;

    private int quantity = 1;

    public UUID getBonusId() {
        return bonusId;
    }

    public void setBonusId(UUID bonusId) {
        this.bonusId = bonusId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BonusProductLinkDto(UUID bonusId, UUID productId, int quantity) {
        this.bonusId = bonusId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public BonusProductLinkDto() {
    }
}
