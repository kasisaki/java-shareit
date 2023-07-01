package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto,
                                                    @RequestHeader(value = SHARER_USER_ID) long userId,
                                                    HttpServletRequest request) {
        log.info("Create Booking request: {}," +
                        "\nFrom User ID: {}" +
                        "\nFrom IP: {}",
                bookingDto, userId, request.getRemoteAddr()
        );
        return new ResponseEntity<>(bookingService.create(bookingDto, userId), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(@PathVariable(name = "bookingId") long bookingId,
                                                     @RequestParam(name = "approved") boolean approved,
                                                     @RequestHeader(value = SHARER_USER_ID) long requestorId,
                                                     HttpServletRequest request) {
        log.info("Requested Booking status change request:" +
                        "\nBooking ID:  {}," +
                        "\nUser ID:    {}" +
                        "\nAPPROVED:    {}" +
                        "\nfrom IP:     {}",
                bookingId, requestorId, approved, request.getRemoteAddr()
        );
        return new ResponseEntity<>(bookingService.approveBooking(bookingId, approved, requestorId), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable(name = "bookingId") long bookingId,
                                                 @RequestHeader(value = SHARER_USER_ID) long requestorId,
                                                 HttpServletRequest request) {
        log.info("GET Booking request:" +
                        "\n     Booking ID: {}," +
                        "\n     Requestor ID:    {}" +
                        "\n     from IP:    {}",
                bookingId, requestorId, request.getRemoteAddr()
        );
        return new ResponseEntity<>(bookingService.getBooking(requestorId, bookingId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<BookingDto>> getUserBookingsState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                 @RequestHeader(value = SHARER_USER_ID) long requestorId,
                                                                 HttpServletRequest request) {
        log.info("GET Bookings request with state:" +
                        "\n     Booking STATE:      {}," +
                        "\n     Requestor ID:       {}" +
                        "\n     from IP:            {}",
                state, requestorId, request.getRemoteAddr()
        );
        return new ResponseEntity<>(bookingService.getUserBookingsState(requestorId, state, from, size), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getUserItemsState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                              @RequestParam(name = "from", defaultValue = "0") int from,
                                                              @RequestParam(name = "size", defaultValue = "10") int size,
                                                              @RequestHeader(value = SHARER_USER_ID) long ownerId,
                                                              HttpServletRequest request) {
        log.info("GET Bookings of ITEM OWNER request with state:" +
                        "\n     Booking STATE:      {}," +
                        "\n     For OWNER ID:       {}" +
                        "\n     from IP:            {}",
                state, ownerId, request.getRemoteAddr()
        );
        return new ResponseEntity<>(bookingService.getUserItemsState(ownerId, state, from, size), HttpStatus.OK);
    }
}
