package com.example.productapp.domain.service;

import com.example.productapp.domain.mapper.ItemMapper;
import com.example.productapp.domain.model.stock.dto.ItemDto;
import com.example.productapp.domain.model.stock.entity.CompositeKey;
import com.example.productapp.domain.model.stock.entity.Item;
import com.example.productapp.domain.repository.ItemRepository;
import com.example.productapp.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService implements AbstractCRUDService<ItemDto, CompositeKey> {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    private static final String ENTITY_NOT_FOUND_MESSAGE = "Entity does not exist";
    private static final String INSUFFICIENT_ITEM_QUANTITY_MESSAGE = "Insufficient quantity of item. Please provide sufficient quantity.";

    @Override
    public ItemDto retrieve(CompositeKey key) {

        return itemMapper.parse(itemRepository.findByNameAndIsSold(key.getName(), key.isSold()).orElseThrow(() ->
                new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE, Item.class.getSimpleName())));
    }

    @Transactional
    @Override
    public ItemDto add(ItemDto itemDto) {

        var item = itemRepository.findByNameAndIsSold(itemDto.getName(), itemDto.isSold());

        if (item.isPresent()) {
            return null;
        }

        return itemMapper.parse(itemRepository.save(itemMapper.parse(itemDto)));
    }

    @Transactional
    @Override
    public ItemDto modify(ItemDto itemDto) {
        var item = itemRepository.findByNameAndIsSold(itemDto.getName(), itemDto.isSold());

        if (item.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE, Item.class.getSimpleName());
        }

        var foundItem = item.get();
        foundItem.setPrice(itemDto.getPrice());
        foundItem.setQuantity(itemDto.getQuantity());
        foundItem.setCompositeKey(CompositeKey.builder()
                .name(itemDto.getName())
                .isSold(itemDto.isSold())
                .build());

        return itemMapper.parse(itemRepository.save(foundItem));
    }

    @Override
    public void remove(CompositeKey key) {

        var item = itemRepository.findByNameAndIsSold(key.getName(), key.isSold());

        if (item.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE, Item.class.getSimpleName());
        }
        itemRepository.delete(item.get());
    }

    public void sellItem(String name, int quantity) {

        var item = itemRepository.findByNameAndIsSold(name, false);

        if (item.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE, Item.class.getSimpleName());
        }

        if (quantity > item.get().getQuantity()) {
            throw new EntityNotFoundException(INSUFFICIENT_ITEM_QUANTITY_MESSAGE, Item.class.getSimpleName());
        }

        var foundItem = item.get();
        foundItem.setQuantity(foundItem.getQuantity() - quantity);

        var soldItemOptional = itemRepository.findByNameAndIsSold(name, true);
        Item soldItem;

        if (soldItemOptional.isEmpty()) {
            soldItem = itemMapper.parse(name, foundItem.getPrice(), quantity, true);
        } else {
            soldItem = soldItemOptional.get();
            soldItem.setQuantity(soldItem.getQuantity() + quantity);
        }

        itemRepository.saveAll(List.of(foundItem, soldItem));
    }
}
