package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.utils.DateUtils.now;

public class ItemRequestMapper {

    public static RequestDto requestToItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest == null) {
            return null;
        }
        return RequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest dtoToItemRequest(RequestDto dto, User requestor) {
        if (dto == null) {
            return null;
        }
        return ItemRequest.builder()
                .description(dto.getDescription())
                .created(now())
                .requestor(requestor)
                .build();
    }
}
