package com.example.productapp.domain.mapper;

import com.example.productapp.domain.model.stock.dto.ItemDto;
import com.example.productapp.domain.model.stock.entity.CompositeKey;
import com.example.productapp.domain.model.stock.entity.Item;
import com.example.productapp.domain.model.stock.request.ItemRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ItemMapper {

    public ItemDto parse(ItemRequest itemRequest) {

        return ItemDto.builder()
                .name(itemRequest.getName())
                .price(itemRequest.getPrice())
                .quantity(itemRequest.getQuantity())
                .isSold(itemRequest.getIsSold())
                .build();
    }

    public ItemDto parse(Item item) {

        return ItemDto.builder()
                .name(item.getCompositeKey().getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .isSold(item.getCompositeKey().isSold())
                .build();
    }

    public Item parse(ItemDto itemDto) {

        return Item.builder()
                .compositeKey(CompositeKey.builder()
                        .name(itemDto.getName())
                        .isSold(itemDto.isSold())
                        .build())
                .price(itemDto.getPrice())
                .quantity(itemDto.getQuantity())
                .build();
    }

    public Item parse(String name, BigDecimal price, int quantity, boolean isSold) {

        return Item.builder()
                .compositeKey(CompositeKey
                        .builder()
                        .name(name)
                        .isSold(isSold)
                        .build())
                .price(price)
                .quantity(quantity)
                .build();
    }
}
