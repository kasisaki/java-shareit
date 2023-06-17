package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.item.model.Item;

public interface RequestStorage {
    Item create(Item request);

    Integer remove(Integer bookingId);

    Integer update(Item request);
}
