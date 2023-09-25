package com.example.productapp.domain.service;

import com.example.productapp.domain.mapper.ItemMapper;
import com.example.productapp.domain.model.stock.dto.ItemDto;
import com.example.productapp.domain.model.stock.dto.SoldItemDto;
import com.example.productapp.domain.model.stock.dto.StockDto;
import com.example.productapp.domain.model.stock.entity.Item;
import com.example.productapp.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StockService implements AbstractReportService<StockDto> {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    @Override
    public StockDto report() {
        List<Item> items = itemRepository.findAll();

        Set<SoldItemDto> soldItems = items.stream()
                .filter(item -> item.getCompositeKey().isSold()
                        || (!item.getCompositeKey().isSold() && item.getQuantity() == 0))
                .map(item -> SoldItemDto.builder()
                        .name(item.getCompositeKey().getName())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toSet());

        List<ItemDto> currentStockItems = items.stream()
                .filter(item -> !item.getCompositeKey().isSold() && item.getQuantity() > 0)
                .map(itemMapper::parse).collect(Collectors.toList());

        return StockDto.builder().soldItems(soldItems).currentStockItems(currentStockItems).build();
    }
}
