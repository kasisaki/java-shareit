package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDto.builder()
                .start(booking.getStart())
                .id(booking.getId())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(booking.getRequestor())
                .item(booking.getItem())
                .itemId(booking.getItem().getId())
                .build();
    }

    public static BookingDtoShort bookingToDtoShort(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDtoShort.builder()
                .id(booking.getId())
                .bookerId(booking.getRequestor().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    public static Booking dtoToBooking(BookingDto dto) {
        if (dto == null) {
            return null;
        }
        Booking booking = new Booking();

        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(dto.getItem());
        booking.setRequestor(dto.getBooker());
        booking.setStatus(dto.getStatus());

        return booking;
    }
}
