package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.util.List;

@Data
@Builder
public class ItemDto {
    BookingDtoShort lastBooking;
    BookingDtoShort nextBooking;
    List<CommentDto> comments;
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Integer request;
}
