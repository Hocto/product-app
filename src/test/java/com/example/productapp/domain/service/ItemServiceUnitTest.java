package com.example.productapp.domain.service;

import com.example.productapp.domain.mapper.ItemMapper;
import com.example.productapp.domain.model.stock.dto.ItemDto;
import com.example.productapp.domain.model.stock.entity.CompositeKey;
import com.example.productapp.domain.model.stock.entity.Item;
import com.example.productapp.domain.repository.ItemRepository;
import com.example.productapp.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.productapp.domain.util.ItemTestUtil.getCompositeKey;
import static com.example.productapp.domain.util.ItemTestUtil.getItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceUnitTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private ItemService itemService;

    @Test
    void onValidItemKeyReturnsItemWithProperties() {

        CompositeKey givenKey = getCompositeKey();

        Item givenItem = getItem(givenKey);

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.of(givenItem));

        when(itemMapper.parse(any(Item.class))).thenReturn(
                ItemDto.builder()
                        .name(givenKey.getName())
                        .price(givenItem.getPrice())
                        .quantity(givenItem.getQuantity())
                        .isSold(givenKey.isSold())
                        .build()
        );

        ItemDto actualResult = itemService.retrieve(givenKey);

        assertThat(actualResult.getName()).isEqualTo(givenKey.getName());
        assertThat(actualResult.isSold()).isEqualTo(givenKey.isSold());
        assertThat(actualResult.getPrice()).isEqualTo(givenItem.getPrice());
        assertThat(actualResult.getQuantity()).isEqualTo(givenItem.getQuantity());
    }

    @Test
    void onNonExistItemKeyReturnsEntityNotFoundException() {

        CompositeKey givenKey = getCompositeKey();

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> itemService.retrieve(givenKey));
    }

    @Test
    void onValidItemPropertiesWithAddMethodReturnsItem() {

        CompositeKey givenKey = getCompositeKey();
        Item givenItem = getItem(givenKey);

        ItemDto givenItemDto = ItemDto.builder()
                .name(givenKey.getName())
                .price(BigDecimal.valueOf(799.99))
                .quantity(1)
                .isSold(givenKey.isSold())
                .build();

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.empty());
        when(itemRepository.save(any(Item.class))).thenReturn(givenItem);

        when(itemMapper.parse(any(Item.class))).thenReturn(givenItemDto);
        when(itemMapper.parse(any(ItemDto.class))).thenReturn(givenItem);

        ItemDto actualResult = itemService.add(givenItemDto);

        assertThat(actualResult.getName()).isEqualTo(givenItemDto.getName());
        assertThat(actualResult.isSold()).isEqualTo(givenItemDto.isSold());
        assertThat(actualResult.getPrice()).isEqualTo(givenItemDto.getPrice());
        assertThat(actualResult.getQuantity()).isEqualTo(givenItemDto.getQuantity());
    }

    @Test
    void onExistingItemWithAddMethodReturnsNull() {

        CompositeKey givenKey = getCompositeKey();
        Item givenItem = getItem(givenKey);

        ItemDto givenItemDto = ItemDto.builder()
                .name(givenKey.getName())
                .price(BigDecimal.valueOf(799.99))
                .quantity(1)
                .isSold(givenKey.isSold())
                .build();

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.of(givenItem));

        ItemDto actualResult = itemService.add(givenItemDto);

        assertThat(actualResult).isNull();
    }

    @Test
    void onValidItemPropertiesWithModifyMethodReturnsItem() {

        CompositeKey givenKey = getCompositeKey();
        Item givenItem = getItem(givenKey);

        ItemDto givenItemDto = ItemDto.builder()
                .name(givenKey.getName())
                .price(BigDecimal.valueOf(999.99))
                .quantity(2)
                .isSold(givenKey.isSold())
                .build();


        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.of(givenItem));
        when(itemRepository.save(any(Item.class))).thenReturn(givenItem);

        when(itemMapper.parse(any(Item.class))).thenReturn(givenItemDto);

        ItemDto actualResult = itemService.modify(givenItemDto);

        assertThat(actualResult.getName()).isEqualTo(givenItemDto.getName());
        assertThat(actualResult.isSold()).isEqualTo(givenItemDto.isSold());
        assertThat(actualResult.getPrice()).isEqualTo(givenItemDto.getPrice());
        assertThat(actualResult.getQuantity()).isEqualTo(givenItemDto.getQuantity());
    }

    @Test
    void onNonExistingItemWithModifyMethodReturnsException() {

        CompositeKey givenKey = getCompositeKey();

        ItemDto givenItemDto = ItemDto.builder()
                .name(givenKey.getName())
                .price(BigDecimal.valueOf(999.99))
                .quantity(2)
                .isSold(givenKey.isSold())
                .build();

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.modify(givenItemDto));
    }

    @Test
    void onExistingItemWithDeleteMethodRemovesItem() {

        CompositeKey givenKey = getCompositeKey();
        Item givenItem = getItem(givenKey);

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.of(givenItem));

        assertDoesNotThrow(() -> itemService.remove(givenKey));
    }

    @Test
    void onNonExistingItemWithDeleteMethodReturnsException() {

        CompositeKey givenKey = getCompositeKey();

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.remove(givenKey));
    }

    @Test
    void onNonExistingItemWithSellItemMethodReturnsException() {

        CompositeKey givenKey = getCompositeKey();

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.sellItem(givenKey.getName(), 1));
    }

    @Test
    void onExistingItemWithLowerQuantitySellItemMethodReturnsException() {

        CompositeKey givenKey = getCompositeKey();
        Item givenItem = getItem(givenKey);

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.of(givenItem));

        assertThrows(EntityNotFoundException.class, () -> itemService.sellItem(givenKey.getName(), 2));
    }

    @Test
    void onExistingItemKeyWithSellItemMethodUpdatesQuantity() {

        CompositeKey givenKey = getCompositeKey();

        CompositeKey givenFoundItemKey = CompositeKey.builder()
                .name("Macbook Pro")
                .isSold(true)
                .build();

        Item givenItem = getItem(givenKey);

        Item foundItem = Item.builder()
                .compositeKey(givenFoundItemKey)
                .price(BigDecimal.valueOf(799.99))
                .quantity(1)
                .build();

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.of(givenItem));
        when(itemRepository.findByNameAndIsSold(givenFoundItemKey.getName(), givenFoundItemKey.isSold())).thenReturn(
                Optional.of(foundItem));
        when(itemRepository.saveAll(List.of(givenItem, foundItem))).thenReturn(List.of(givenItem, foundItem));

        assertDoesNotThrow(() -> itemService.sellItem(givenKey.getName(), givenItem.getQuantity()));
    }

    @Test
    void onExistingItemKeyWithSellItemMethodCreateNewItem() {

        CompositeKey givenKey = getCompositeKey();

        CompositeKey givenFoundItemKey = CompositeKey.builder()
                .name("Macbook Pro")
                .isSold(true)
                .build();

        Item givenItem = getItem(givenKey);

        Item foundItem = Item.builder()
                .compositeKey(givenFoundItemKey)
                .price(BigDecimal.valueOf(799.99))
                .quantity(1)
                .build();

        when(itemRepository.findByNameAndIsSold(givenKey.getName(), givenKey.isSold())).thenReturn(
                Optional.of(givenItem));
        when(itemRepository.findByNameAndIsSold(givenFoundItemKey.getName(), givenFoundItemKey.isSold())).thenReturn(
                Optional.empty());

        when(itemMapper.parse(any(String.class), any(BigDecimal.class), any(int.class), any(Boolean.class)))
                .thenReturn(givenItem);
        when(itemRepository.saveAll(any())).thenReturn(List.of(givenItem, foundItem));

        assertDoesNotThrow(() -> itemService.sellItem(givenKey.getName(), givenItem.getQuantity()));
    }
}
