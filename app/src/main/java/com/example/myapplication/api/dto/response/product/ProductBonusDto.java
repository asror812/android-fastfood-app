package com.example.myapplication.api.dto.response.product;

import java.time.LocalDate;
import java.util.UUID;

public class ProductBonusDto {
    private UUID id;

    protected String name;

    protected LocalDate startDate;

    protected LocalDate endDate;

    protected BonusConditionResponseDto condition;

    protected int usageLimit;
}
