package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemInMemoryStorage implements ItemStorage {

    private final HashMap<Integer, Item> items = new HashMap<>();
    private Integer id = 0;

    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Integer removeItem(Integer itemId) {
        items.remove(itemId);
        return itemId;
    }

    @Override
    public Integer updateItem(ItemUpdateDto itemDto, Integer itemId, Integer ownerId) {
        updateIfNotNull(itemDto, itemId);
        return itemId;
    }

    @Override
    public Item getItem(Integer itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    private void updateIfNotNull(ItemUpdateDto itemDto, Integer itemId) {
        if (itemDto.getOwner() != null) {
            items.get(itemId).setOwner(itemDto.getOwner());
        }
        if (itemDto.getName() != null) {
            items.get(itemId).setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            items.get(itemId).setDescription(itemDto.getDescription());
        }
        if (itemDto.getRequest() != null) {
            items.get(itemId).setRequest(itemDto.getRequest());
        }
        if (itemDto.getAvailable() != null) {
            items.get(itemId).setAvailable(itemDto.getAvailable());
        }

    }

}
