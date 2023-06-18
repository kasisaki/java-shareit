package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;

import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        return new ResponseEntity<>(bookingService.createBooking(bookingDto), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}?approved={approved}")
    public ResponseEntity<BookingDto> approveBooking(@PathVariable(name = "bookingId") long bookingId,
                                                     @PathVariable(name = "approved") boolean approved) {
        return new ResponseEntity<>(bookingService.approveBooking(bookingId, approved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable(name = "bookingId") long bookingId,
                                                 @RequestHeader(value = SHARER_USER_ID) long requesterId) {
        return new ResponseEntity<>(bookingService.getBooking(requesterId, bookingId), HttpStatus.OK);
    }
}
