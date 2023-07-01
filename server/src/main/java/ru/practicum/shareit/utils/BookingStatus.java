package ru.practicum.shareit.utils;

import ru.practicum.shareit.exception.IllegalStatusException;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED;

    public static BookingStatus ofString(String str) {
        switch (str) {
            case "REJECTED":
                return REJECTED;
            case "APPROVED":
                return APPROVED;
            case "CANCELED":
                return CANCELED;
            case "WAITING":
                return WAITING;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
