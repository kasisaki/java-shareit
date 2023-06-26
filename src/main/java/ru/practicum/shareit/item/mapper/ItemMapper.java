package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item, BookingDtoShort nextBooking, BookingDtoShort lastBooking, List<CommentDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    public static Item updateItemWithDto(Item item, ItemUpdateDto itemDto) {
        if (itemDto.getOwner() != null) {
            item.setOwner(itemDto.getOwner());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }
}
