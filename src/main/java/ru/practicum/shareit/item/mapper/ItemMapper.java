package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item, BookingDtoShort nextBooking, BookingDtoShort lastBooking, List<CommentDto> comments) {
        if (item == null) {
            return null;
        }

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .requestId(item.getRequestId())
                .build();
    }

    public static Item updateItemWithDto(Item item, ItemUpdateDto itemDto) {
        if (item == null) {
            return null;
        }
        if (itemDto == null) {
            return item;
        }
        if (itemDto.getOwner() != null) {
            item.setOwner(itemDto.getOwner());
        }
        if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequestId() != null) {
            item.setRequestId(itemDto.getRequestId());
        }
        return item;
    }

    public static ItemDtoShort itemToDtoShort(Item item) {
        if (item == null) {
            return null;
        }

        return ItemDtoShort.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .requestId(item.getRequestId())
                .available(item.getAvailable())
                .description(item.getDescription())
                .build();
    }
}
