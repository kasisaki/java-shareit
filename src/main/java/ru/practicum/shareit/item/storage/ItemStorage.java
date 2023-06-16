package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(ItemUpdateDto itemUpdateDto);

    Integer removeItem(Integer itemId);

    Item updateItem(ItemUpdateDto item, Integer itemId);

    Item getItem(Integer itemId);

    List<Item> getAllItems();
}
