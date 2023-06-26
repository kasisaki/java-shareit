package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.shareit.CommonData.*;
import static ru.practicum.shareit.booking.BookingMapper.*;

@SpringBootTest
public class BookingMapperTests {

    @Test
    public void testBookingToBookingDto() throws Exception {
        assertEquals(bookingRejected.getId(), toBookingDto(bookingRejected).getId());
        assertEquals(bookingRejected.getStart(), toBookingDto(bookingRejected).getStart());
        assertEquals(bookingRejected.getEnd(), toBookingDto(bookingRejected).getEnd());
        assertEquals(bookingRejected.getStatus(), toBookingDto(bookingRejected).getStatus());
        assertEquals(bookingRejected.getItem(), toBookingDto(bookingRejected).getItem());
        assertEquals(bookingRejected.getItem().getId(), toBookingDto(bookingRejected).getItemId());
        assertEquals(bookingRejected.getRequestor(), toBookingDto(bookingRejected).getBooker());
    }

    @Test
    public void testBookingToBookingDtoNull() throws Exception {
        assertNull(toBookingDto(null));
    }


    @Test
    public void testDtoToBooking() throws Exception {
        assertEquals(bookingDto1.getStart(), dtoToBooking(bookingDto1).getStart());
        assertEquals(bookingDto1.getEnd(), dtoToBooking(bookingDto1).getEnd());
        assertEquals(bookingDto1.getItem(), dtoToBooking(bookingDto1).getItem());
        assertEquals(bookingDto1.getStatus(), dtoToBooking(bookingDto1).getStatus());
    }

    @Test
    public void testDtoToBookingNull() throws Exception {
        assertNull(dtoToBooking(null));
    }

    @Test
    public void testDtoToDtoShort() throws Exception {
        assertEquals(bookingApproved.getStart(), bookingToDtoShort(bookingApproved).getStart());
        assertEquals(bookingApproved.getEnd(), bookingToDtoShort(bookingApproved).getEnd());
        assertEquals(bookingApproved.getRequestor().getId(), bookingToDtoShort(bookingApproved).getBookerId());
        assertEquals(bookingApproved.getId(), bookingToDtoShort(bookingApproved).getId());
    }

    @Test
    public void testDtoToDtoShortNull() throws Exception {
        assertNull(bookingToDtoShort(null));
    }
}
