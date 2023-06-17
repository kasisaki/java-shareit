package ru.practicum.shareit.utils;

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
            default:
                return WAITING;
        }
    }
}
