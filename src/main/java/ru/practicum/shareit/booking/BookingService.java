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

    public BookingDto approveBooking(long bookingId, boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Booking with id " + bookingId + "not found"));
        if (booking.getRequestor().getId().equals(userId)) {
            throw new ElementNotFoundException("Requester cannot approve requested booking");
        } else if (booking.getRequestor().getId().equals(booking.getItem().getOwner().getId())) {
            if (booking.getStatus().equals(APPROVED)) {
                throw new BadRequestException("Already APPROVED");
            } else {
                if (approved) {
                    booking.setStatus(APPROVED);
                } else {
                    booking.setStatus(REJECTED);
                }
            }
        } else {
            throw new BadRequestException("Not allowed");
        }

        return toBookingDto(booking);
    }

    public BookingDto getBooking(long requesterId, long bookingId) {
        if (!userRepository.existsById(requesterId)) {
            throw new ElementNotFoundException("User does not exist");
        }
        return toBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Booking with id " + bookingId + "not found")));
    }

    public List<BookingDto> getUserBookingsState(long ownerId, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookingRepository
                        .findAllByRequestorId(ownerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository
                        .findAllByRequestorIdAndStartBeforeAndEndAfterAndStatusIs(ownerId, now, now, APPROVED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository
                        .findAllByRequestorIdAndStartAfterAndEndAfterAndStatusIs(ownerId, now, now, APPROVED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository
                        .findAllByRequestorIdAndStartBeforeAndEndBeforeAndStatusIs(ownerId, now, now, APPROVED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return getByStatus(ownerId, REJECTED);
            case "WAITING":
                return getByStatus(ownerId, WAITING);

        }
        return null;
    }

    public List<BookingDto> getUserItemsState(long ownerId, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookingRepository
                        .findAllByRequestorId(ownerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository
                        .findAllByRequestorIdAndStartBeforeAndEndAfterAndStatusIs(ownerId, now, now, APPROVED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository
                        .findAllByRequestorIdAndStartAfterAndEndAfterAndStatusIs(ownerId, now, now, APPROVED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return getByStatus(ownerId, REJECTED);
            case "WAITING":
                return getByStatus(ownerId, WAITING);

        }
        throw new IllegalStateException("Unknown state: UNSUPPORTED_STATUS");
    }

    private List<BookingDto> getByStatus(Long ownerId, BookingStatus status) {
        return bookingRepository
                .findAllByRequestorIdAndStatusIs(ownerId, status)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
