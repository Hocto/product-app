package com.example.productapp.domain.model.stock.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SoldItemDto {

    private String name;
    private int quantity;
}
