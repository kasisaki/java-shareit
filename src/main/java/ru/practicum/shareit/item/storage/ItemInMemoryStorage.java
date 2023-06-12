package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
    public Item createItem(Item item, User owner) {
        item.setOwner(owner);
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
    public Integer updateItem(Item item) {
        items.put(item.getId(), item);
        return item.getId();
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
