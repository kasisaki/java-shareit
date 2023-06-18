package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.ItemRequest;

public interface RequestStorage {
    ItemRequest create(ItemRequest request);

    Long remove(Long bookingId);

    Long update(ItemRequest request);
}
