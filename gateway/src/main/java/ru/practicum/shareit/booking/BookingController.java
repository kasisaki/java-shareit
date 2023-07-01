package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> bookItem(@Valid @RequestBody BookingDto bookingDto,
                                           @RequestHeader(value = SHARER_USER_ID) long userId,
                                           HttpServletRequest request) {
        log.info("Create Booking request: {}," +
                        "\nFrom User ID: {}" +
                        "\nFrom IP: {}",
                bookingDto, userId, request.getRemoteAddr()
        );
        return bookingClient.bookItem(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable(name = "bookingId") long bookingId,
                                                 @RequestParam(name = "approved") boolean approved,
                                                 @RequestHeader(value = SHARER_USER_ID) long requestorId,
                                                 HttpServletRequest request) {
        log.info("Requested Booking status change request:" +
                        "\nBooking ID:  {}," +
                        "\nUser ID:     {}" +
                        "\nAPPROVED:    {}" +
                        "\nfrom IP:     {}",
                bookingId, requestorId, approved, request.getRemoteAddr()
        );
        return bookingClient.approveBooking(requestorId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable(name = "bookingId") long bookingId,
                                             @RequestHeader(value = SHARER_USER_ID) long requestorId,
                                             HttpServletRequest request) {
        log.info("GET Booking request:" +
                        "\n     Booking ID: {}," +
                        "\n     Requestor ID:    {}" +
                        "\n     from IP:    {}",
                bookingId, requestorId, request.getRemoteAddr()
        );
        return bookingClient.getBooking(requestorId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getUserBookingsState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                       @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                                       @RequestHeader(value = SHARER_USER_ID) long requestorId,
                                                       HttpServletRequest request) {
        log.info("GET Bookings request with state:" +
                        "\n     Booking STATE:      {}," +
                        "\n     Requestor ID:       {}" +
                        "\n     from IP:            {}",
                state, requestorId, request.getRemoteAddr()
        );
        return bookingClient.getBookings(requestorId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getUserItemsBookings(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                       @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                                       @RequestHeader(value = SHARER_USER_ID) long ownerId,
                                                       HttpServletRequest request) {
        log.info("GET Bookings of ITEM OWNER request with state:" +
                        "\n     Booking STATE:      {}," +
                        "\n     For OWNER ID:       {}" +
                        "\n     from IP:            {}",
                state, ownerId, request.getRemoteAddr()
        );
        return bookingClient.getUserItemsBookings(ownerId, state, from, size);
    }
}
