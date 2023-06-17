package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;

public class RequestInMemoryStorage implements RequestStorage {

    private final HashMap<Integer, Item> bookings = new HashMap<>();

    public Item create(Item request) {
        bookings.put(request.getId(), request);
        return request;
    }

    public Integer remove(Integer bookingId) {
        bookings.remove(bookingId);
        return bookingId;
    }

    public Integer update(Item request) {
        bookings.put(request.getId(), request);
        return request.getId();
    }
}
