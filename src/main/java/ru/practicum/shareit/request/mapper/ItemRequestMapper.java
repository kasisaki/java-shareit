package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public class ItemRequestMapper {

    public static ItemRequestDto itemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester())
                .dateCreated(itemRequest.getDateCreated())
                .build();
    }
}
