package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemInMemoryStorage implements ItemStorage {

    private final Map<Integer, Item> items = new HashMap<>();
    private Integer id = 0;

    @Override
    public Item createItem(ItemUpdateDto itemUpdateDto) {
        Item newItem = Item.builder().build();
        newItem.setId(++id);
        items.put(newItem.getId(), ItemMapper.updateItemWithDto(newItem, itemUpdateDto));
        return newItem;
    }

    @Override
    public Integer removeItem(Integer itemId) {
        items.remove(itemId);
        return itemId;
    }

    @Override
    public Item updateItem(ItemUpdateDto itemDto, Integer itemId) {
        return ItemMapper.updateItemWithDto(items.get(itemId), itemDto);
    }

    @Override
    public Item getItem(Integer itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }



}
