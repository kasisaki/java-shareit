package ru.practicum.shareit.utils;

public enum BookingState {
    WAITING,
    CURRENT,
    REJECTED,
    FUTURE;

    public static BookingState ofString(String str) {
        switch (str) {
            case "REJECTED":
                return REJECTED;
            case "CURRENT":
                return CURRENT;
            case "FUTURE":
                return FUTURE;
            default:
                return WAITING;
        }
    }
}
