package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.IllegalStatusException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.BookingStatus;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingMapper.toBookingDto;
import static ru.practicum.shareit.utils.BookingStatus.*;
import static ru.practicum.shareit.utils.DateUtils.now;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public BookingDto create(BookingDto bookingDto, Long userId) {
        if (!bookingDto.getEnd().isAfter(bookingDto.getStart())) {
            throw new BadRequestException("Booking end time must not be before the start time");
        }
        bookingDto.setItem(itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ElementNotFoundException("Item with id " + " not found")));
        bookingDto.setBooker(userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("User with id " + userId + " is not known")));
        if (bookingDto.getItem().getOwner().getId().equals(userId)) {
            throw new ElementNotFoundException("Cannot book your own item");
        }
        bookingDto.setStatus(WAITING);
        if (bookingDto.getItem().getAvailable()) {
            return toBookingDto(bookingRepository.save(BookingMapper.dtoToBooking(bookingDto)));
        }
        throw new BadRequestException("Item is not available");
    }

    @Transactional
    public BookingDto approveBooking(Long bookingId, boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Booking with id " + bookingId + "not found"));
        if (booking.getRequestor().getId().equals(userId)) {
            throw new ElementNotFoundException("Requester cannot approve requested booking");
        }
        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (booking.getStatus().equals(APPROVED)) {
                throw new BadRequestException("Already APPROVED");
            } else {
                if (approved) {
                    booking.setStatus(APPROVED);
                } else {
                    booking.setStatus(REJECTED);
                }
                return toBookingDto(bookingRepository.save(booking));
            }
        } else {
            throw new BadRequestException("Not allowed");
        }
    }

    public BookingDto getBooking(Long requestorId, Long bookingId) {
        userExistOrThrow(requestorId);

        if (!bookingRepository.existsByIdAndRequestorId(bookingId, requestorId)) {
            if (!bookingRepository.existsByIdAndItemOwnerId(bookingId, requestorId)) {
                throw new ElementNotFoundException("This user does not have requested booking");
            }
        }
        return toBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Booking with id " + bookingId + "not found")));
    }

    public List<BookingDto> getUserBookingsState(Long requestorId, String state, Integer from, Integer size) {
        userExistOrThrow(requestorId);
        Pageable pageable = PageRequest.of(from / size, size);

        switch (state) {
            case "ALL":
                return bookingRepository
                        .findAllByRequestorIdOrderByStartDesc(requestorId,
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository
                        .findAllByRequestorIdAndStartBeforeAndEndAfterOrderByStartDesc(requestorId, now(), now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository
                        .findAllByRequestorIdAndStartAfterOrderByStartDesc(requestorId, now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository
                        .findAllByRequestorIdAndEndBeforeOrderByStartDesc(requestorId, now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return getByStatusAndRequestor(requestorId, REJECTED, pageable);
            case "WAITING":
                return getByStatusAndRequestor(requestorId, WAITING, pageable);
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");

        }

    }

    public List<BookingDto> getUserItemsState(Long ownerId, String state, Integer from, Integer size) {
        userExistOrThrow(ownerId);
        Pageable pageable = PageRequest.of(from / size, size);

        switch (state) {
            case "ALL":
                return bookingRepository
                        .findAllByItemOwnerIdOrderByStartDesc(ownerId, pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case "CURRENT":
                return bookingRepository
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now(), now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository
                        .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository
                        .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return getByStatusAndItemOwner(ownerId, REJECTED, pageable);
            case "WAITING":
                return getByStatusAndItemOwner(ownerId, WAITING, pageable);
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private List<BookingDto> getByStatusAndRequestor(Long requestorId, BookingStatus status, Pageable pageable) {
        return bookingRepository
                .findAllByRequestorIdAndStatusIsOrderByStartDesc(requestorId, status, pageable)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<BookingDto> getByStatusAndItemOwner(Long itemOwnerId, BookingStatus status, Pageable pageable) {
        return bookingRepository
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(itemOwnerId, status, pageable)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void userExistOrThrow(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ElementNotFoundException("User with id " + userId + " does not exist");
        }
    }
}
