package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.item.model.Item;

public interface BookingStorage {
    Item create(Item booking);

    Integer remove(Integer itemId);

    Integer update(Item booking);
}
