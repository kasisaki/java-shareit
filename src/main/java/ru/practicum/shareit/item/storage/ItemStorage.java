package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(Item item);

    Integer removeItem(Integer itemId);

    Integer updateItem(ItemUpdateDto item, Integer itemId, Integer ownerId);

    Item getItem(Integer itemId);

    List<Item> getAllItems();
}
