package com.example.productapp.domain.service;

import com.example.productapp.domain.mapper.ItemMapper;
import com.example.productapp.domain.model.stock.dto.ItemDto;
import com.example.productapp.domain.model.stock.dto.StockDto;
import com.example.productapp.domain.model.stock.entity.Item;
import com.example.productapp.domain.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.example.productapp.domain.util.ItemTestUtil.getItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceUnitTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private StockService stockService;

    @Test
    void onExistingItemsReturnsSoldAndCurrentItems() {
        when(itemRepository.findAll()).thenReturn(getItems());

        when(itemMapper.parse(any(Item.class))).thenReturn(
                ItemDto.builder()
                        .name("Macbook Pro")
                        .isSold(false)
                        .price(BigDecimal.valueOf(799.99))
                        .quantity(3)
                        .build()
        );


        StockDto actualResult = stockService.report();

        assertThat(actualResult.getSoldItems()).hasSize(2);
        assertThat(actualResult.getCurrentStockItems()).hasSize(1);
        assertThat(actualResult.getCurrentStockItems().get(0).getName()).isEqualTo("Macbook Pro");
        assertThat(actualResult.getCurrentStockItems().get(0).getPrice()).isEqualTo(BigDecimal.valueOf(799.99));
        assertThat(actualResult.getCurrentStockItems().get(0).getQuantity()).isEqualTo(3);
        assertThat(actualResult.getCurrentStockItems().get(0).isSold()).isFalse();
    }
}
