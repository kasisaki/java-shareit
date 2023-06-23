package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.IllegalStatusException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDto;
import static ru.practicum.shareit.utils.BookingStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

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

    public BookingDto getBooking(Long requesterId, Long bookingId) {
        if (!userRepository.existsById(requesterId)) {
            throw new ElementNotFoundException("User does not exist");
        }

        if (!bookingRepository.existsByIdAndRequestorId(bookingId, requesterId)) {
            if (!bookingRepository.existsByIdAndItemOwnerId(bookingId, requesterId)) {
                throw new ElementNotFoundException("This user does not have requested booking");
            }
        }
        return toBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Booking with id " + bookingId + "not found")));
    }

    public List<BookingDto> getUserBookingsState(Long requestorId, String state) {
        if (!userRepository.existsById(requestorId)) {
            throw new ElementNotFoundException("User does not exist");
        }
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookingRepository
                        .findAllByRequestorIdOrderByStartDesc(requestorId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository
                        .findAllByRequestorIdAndStartBeforeAndEndAfterOrderByStartDesc(requestorId, now, now)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository
                        .findAllByRequestorIdAndStartAfterOrderByStartDesc(requestorId, now)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository
                        .findAllByRequestorIdAndEndBeforeOrderByStartDesc(requestorId, now)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return getByStatusAndRequestor(requestorId, REJECTED);
            case "WAITING":
                return getByStatusAndRequestor(requestorId, WAITING);
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");

        }

    }

    public List<BookingDto> getUserItemsState(Long ownerId, String state) {
        if (!userRepository.existsById(ownerId)) {
            throw new ElementNotFoundException("User does not exist");
        }

        LocalDateTime now = LocalDateTime.now();
        log.warn("THE STATE IS --------" + state);

        switch (state) {
            case "ALL":
                return bookingRepository
                        .findAllByItemOwnerIdOrderByStartDesc(ownerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case "CURRENT":
                return bookingRepository
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now, now)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository
                        .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository
                        .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return getByStatusAndItemOwner(ownerId, REJECTED);
            case "WAITING":
                return getByStatusAndItemOwner(ownerId, WAITING);
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private List<BookingDto> getByStatusAndRequestor(Long requestorId, BookingStatus status) {
        return bookingRepository
                .findAllByRequestorIdAndStatusIsOrderByStartDesc(requestorId, status)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<BookingDto> getByStatusAndItemOwner(Long itemOwnerId, BookingStatus status) {
        return bookingRepository
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(itemOwnerId, status)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
