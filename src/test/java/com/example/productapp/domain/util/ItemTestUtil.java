package com.example.productapp.domain.util;

import com.example.productapp.domain.model.stock.entity.CompositeKey;
import com.example.productapp.domain.model.stock.entity.Item;

import java.math.BigDecimal;
import java.util.List;

public class ItemTestUtil {

    public static List<Item> getItems() {

        return List.of(Item.builder().compositeKey(CompositeKey.builder()
                                .name("Macbook Pro")
                                .isSold(false)
                                .build())
                        .price(BigDecimal.valueOf(799.99))
                        .quantity(3)
                        .build(),
                Item.builder().compositeKey(CompositeKey.builder()
                                .name("Apple Watch 8")
                                .isSold(true)
                                .build())
                        .price(BigDecimal.valueOf(499.99))
                        .quantity(3)
                        .build(),
                Item.builder().compositeKey(CompositeKey.builder()
                                .name("Macbook Air")
                                .isSold(true)
                                .build())
                        .price(BigDecimal.valueOf(699.99))
                        .quantity(1)
                        .build());
    }

    public static CompositeKey getCompositeKey() {
        return CompositeKey.builder()
                .name("Macbook Pro")
                .isSold(false)
                .build();
    }

    public static Item getItem(CompositeKey key) {
        return Item.builder()
                .compositeKey(key)
                .price(BigDecimal.valueOf(799.99))
                .quantity(1)
                .build();
    }
}
