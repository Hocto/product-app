package com.example.productapp.domain.model.stock.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ItemDto {

    private String name;
    private BigDecimal price;
    private int quantity;
    private boolean isSold;

}
