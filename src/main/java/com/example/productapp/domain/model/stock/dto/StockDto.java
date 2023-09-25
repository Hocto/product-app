package com.example.productapp.domain.model.stock.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class StockDto {

    Set<SoldItemDto> soldItems;
    List<ItemDto> currentStockItems;
}
