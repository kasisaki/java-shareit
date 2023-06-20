package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.BookingStatus;

@Data
@Builder
public class BookingResponseDto {
    private Long id;
    private Long itemId;
    private Item item;
    private User booker;
    private BookingStatus status;
}
