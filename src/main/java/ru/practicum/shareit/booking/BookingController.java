package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto,
                                                    @RequestHeader(value = SHARER_USER_ID) long userId) {
        return new ResponseEntity<>(bookingService.create(bookingDto, userId), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(@PathVariable(name = "bookingId") long bookingId,
                                                     @RequestParam(name = "approved") boolean approved,
                                                     @RequestHeader(value = SHARER_USER_ID) long requesterId) {
        return new ResponseEntity<>(bookingService.approveBooking(bookingId, approved, requesterId), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable(name = "bookingId") long bookingId,
                                                 @RequestHeader(value = SHARER_USER_ID) long requesterId) {
        return new ResponseEntity<>(bookingService.getBooking(requesterId, bookingId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<BookingDto>> getUserBookingsState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                                 @RequestHeader(value = SHARER_USER_ID) long ownerId) {
        return new ResponseEntity<>(bookingService.getUserBookingsState(ownerId, state), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getUserItemsState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                              @RequestHeader(value = SHARER_USER_ID) long ownerId) {
        return new ResponseEntity<>(bookingService.getUserItemsState(ownerId, state), HttpStatus.OK);
    }
}
