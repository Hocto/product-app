package com.example.productapp.domain.mapper;

import com.example.productapp.domain.model.stock.dto.ItemDto;
import com.example.productapp.domain.model.stock.entity.CompositeKey;
import com.example.productapp.domain.model.stock.entity.Item;
import com.example.productapp.domain.model.stock.request.ItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ItemMapperUnitTest {

    private final ItemMapper itemMapper = new ItemMapper();

    @Test
    void onDtoModelToEntity() {

        Item actualResult = itemMapper.parse(ItemDto.builder()
                .name("Macbook Pro")
                .isSold(true)
                .price(BigDecimal.valueOf(799.99))
                .quantity(1)
                .build());

        assertThat(actualResult.getCompositeKey().getName()).isEqualTo("Macbook Pro");
        assertThat(actualResult.getCompositeKey().isSold()).isTrue();
        assertThat(actualResult.getPrice()).isEqualTo(BigDecimal.valueOf(799.99));
        assertThat(actualResult.getQuantity()).isEqualTo(1);
    }

    @Test
    void onEntityToDtoModel() {

        ItemDto actualResult = itemMapper.parse(Item.builder()
                        .compositeKey(CompositeKey.builder()
                                .name("Macbook Pro")
                                .isSold(false)
                                .build())
                .price(BigDecimal.valueOf(799.99))
                .quantity(1)
                .build());

        assertThat(actualResult.getName()).isEqualTo("Macbook Pro");
        assertThat(actualResult.isSold()).isFalse();
        assertThat(actualResult.getPrice()).isEqualTo(BigDecimal.valueOf(799.99));
        assertThat(actualResult.getQuantity()).isEqualTo(1);
    }

    @Test
    void onRequestModelToDtoModel() {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Macbook Pro");
        itemRequest.setPrice(BigDecimal.valueOf(799.99));
        itemRequest.setQuantity(1);
        itemRequest.setIsSold(true);

        ItemDto actualResult = itemMapper.parse(itemRequest);

        assertThat(actualResult.getName()).isEqualTo("Macbook Pro");
        assertThat(actualResult.isSold()).isTrue();
        assertThat(actualResult.getPrice()).isEqualTo(BigDecimal.valueOf(799.99));
        assertThat(actualResult.getQuantity()).isEqualTo(1);
    }

    @Test
    void onMultipleParamsToEntity() {

        String givenName = "Macbook Pro";
        BigDecimal givenPrice = BigDecimal.valueOf(799.99);
        int givenQuantity = 1;
        boolean givenIsSold = false;

        Item actualResult = itemMapper.parse(givenName, givenPrice, givenQuantity, givenIsSold);

        assertThat(actualResult.getCompositeKey().getName()).isEqualTo("Macbook Pro");
        assertThat(actualResult.getCompositeKey().isSold()).isFalse();
        assertThat(actualResult.getPrice()).isEqualTo(BigDecimal.valueOf(799.99));
        assertThat(actualResult.getQuantity()).isEqualTo(1);
    }
}
