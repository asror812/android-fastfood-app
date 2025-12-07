package com.example.myapplication.api.dto.response;

import com.example.myapplication.api.dto.BonusProductLinkDto;

import java.util.UUID;

public class BonusProductLinkResponseDTO extends BonusProductLinkDto {
    private UUID id;
    private String bonusName;

    public BonusProductLinkResponseDTO(UUID id, String bonusName, UUID bonusId, UUID productId, int quantity) {
        super(bonusId, productId, quantity);
        this.id = id;
        this.bonusName = bonusName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
