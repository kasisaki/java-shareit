package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;

public class BookingInMemoryStorage implements BookingStorage {

    private HashMap<Integer, Item> bookings = new HashMap<>();
    public Item create(Item booking) {
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public Integer remove(Integer bookingId) {
        bookings.remove(bookingId);
        return bookingId;
    }

    public Integer update(Item booking) {
        bookings.put(booking.getId(), booking);
        return booking.getId();
    }
}
