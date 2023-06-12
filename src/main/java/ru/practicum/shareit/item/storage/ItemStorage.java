package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {
    Item createItem(Item item, User owner);

    Integer removeItem(Integer itemId);

    Integer updateItem(Item item);

    Item getItem(Integer itemId);

    List<Item> getAllItems();
}
