package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemComment;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingMapper.bookingToDtoShort;
import static ru.practicum.shareit.item.mapper.CommentMapper.mapCommentRequestDtoToComment;
import static ru.practicum.shareit.item.mapper.CommentMapper.mapCommentToCommentResponseDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.utils.BookingStatus.APPROVED;
import static ru.practicum.shareit.utils.DateUtils.now;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ItemDto create(ItemUpdateDto itemUpdateDto, Long ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new ElementNotFoundException("User with id " + ownerId + " is not found"));
        itemUpdateDto.setOwner(user);
        Item item = new Item();
        return retrieveWithBookingInfo(itemRepository.save(ItemMapper.updateItemWithDto(item, itemUpdateDto)));
    }

    @Transactional
    public ItemDto updateItem(Long itemId, ItemUpdateDto itemUpdateDto, Long ownerId) {
        Item item = itemRepository.save(ItemMapper.updateItemWithDto(
                checkItemAndUserExistAndReturn(ownerId, itemId), itemUpdateDto));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new BadRequestException("User with id " + ownerId + " doest not own item " + itemId);
        }
        return retrieveWithBookingInfo(item);
    }

    public ItemDto getItem(Long itemId, Long ownerId) {
        ItemDto result = retrieveWithBookingInfo(checkItemAndUserExistAndReturn(ownerId, itemId));
        if (!itemRepository.existsByIdAndOwnerId(itemId, ownerId)) {
            result.setLastBooking(null);
            result.setNextBooking(null);
        }

        return result;
    }

    public List<ItemDto> getItemsOfOwner(Long ownerId, Integer from, Integer size) {
        if (!userRepository.existsById(ownerId)) {
            throw new ElementNotFoundException("User with id " + ownerId + " does not exist");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        // Поля lastBooking и nextBooking должны постоянно обновляться,
        // т.к. высчитываются по времени запроса
        // можно вместо множественного запроса за раз получить все данные по комментариям и бронированиям всех вещей пользователя
        // и уже распределять по item, но тут уже возникает проблема объема получаемых данных
        // по другому не знаю как. Если это оптимальнее - могу реализовать
        return itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId, pageable).stream().map(this::retrieveWithBookingInfo)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String searchStr, Integer from, Integer size) {
        if (searchStr.isEmpty()) {
            return new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(from / size, size);

        return itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableTrue(searchStr, pageable).stream()
                .map(this::retrieveWithBookingInfo)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createItemComment(CommentDto commentDto, Long userId, Long itemId) {
        if (!bookingRepository.existsByItemIdAndRequestorIdAndStartIsBefore(itemId, userId, LocalDateTime.now())) {
            throw new BadRequestException("User with id = " + userId + "has not booked the item with id = " + itemId);
        }

        User user = userRepository
                .findById(userId).orElseThrow(() -> new ElementNotFoundException("User " + userId + "not found"));
        Item item = itemRepository
                .findById(itemId).orElseThrow(() -> new ElementNotFoundException("Item " + itemId + "not found"));

        return mapCommentToCommentResponseDto(commentRepository
                .save(mapCommentRequestDtoToComment(commentDto, item, user)));
    }

    private Item checkItemAndUserExistAndReturn(Long ownerId, Long itemId) {
        if (!userRepository.existsById(ownerId)) {
            throw new ElementNotFoundException("User with id " + ownerId + " not found");
        }

        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ElementNotFoundException("Item with id " + itemId + " does not exist"));
    }

    private ItemDto retrieveWithBookingInfo(Item item) {
        List<ItemComment> comments = commentRepository.findByItemId(item.getId());

        BookingDtoShort lastBooking = bookingToDtoShort(bookingRepository
                .findFirstByStatusAndItemIdAndStartIsBeforeOrderByStartDesc(APPROVED, item.getId(), now()));

        BookingDtoShort nextBooking = bookingToDtoShort(bookingRepository
                .findFirstByStatusAndItemIdAndStartIsAfterOrderByStartAsc(APPROVED, item.getId(), now()));

        return toItemDto(item, nextBooking, lastBooking,
                comments.stream().map(CommentMapper::mapCommentToCommentResponseDto).collect(Collectors.toList()));
    }
}
