package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.mapper.ItemRequestMapper.dtoToItemRequest;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.requestToItemRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    public RequestDto create(RequestDto requestDto, Long requestorId) {
        return requestToItemRequestDto(itemRequestRepository.save(dtoToItemRequest(requestDto, userRepository.findById(requestorId).orElseThrow(
                () -> new ElementNotFoundException("User " + requestorId + " does not exist")
        ))));
    }

    public List<RequestDto> getAllRequestsOfUser(Long requestorId) {
        userExistsOrThrow(requestorId);
        return itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(requestorId)
                .stream()
                .map(ItemRequestMapper::requestToItemRequestDto)
                .map(this::setItems)
                .collect(Collectors.toList());
    }

    public List<RequestDto> getAllRequests(Long requestorId, Integer from, Integer size) {
        return itemRequestRepository.findAllByRequestorIdIsNotOrderByCreatedAsc(requestorId, PageRequest.of(from, size))
                .stream()
                .map(ItemRequestMapper::requestToItemRequestDto)
                .map(this::setItems)
                .collect(Collectors.toList());
    }

    public RequestDto getRequest(Long requestId, Long requestorId) {
        userExistsOrThrow(requestorId);
        return setItems(requestToItemRequestDto(itemRequestRepository.findById(requestId).orElseThrow(() -> new ElementNotFoundException(
                "Request with id " + requestId + "not found")
        )));
    }

    private void userExistsOrThrow(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ElementNotFoundException("User " + userId + "not found");
        }
    }

    private RequestDto setItems(RequestDto dto) {
        dto.setItems(itemRepository.findAllByRequestId(dto.getId())
                .stream()
                .map(ItemMapper::itemToDtoShort)
                .collect(Collectors.toList()));
        return dto;
    }
}
