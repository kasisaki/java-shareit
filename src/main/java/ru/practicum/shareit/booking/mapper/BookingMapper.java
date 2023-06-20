package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
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

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .booker(booking.getRequestor())
                .item(booking.getItem())
                .itemId(booking.getItem().getId())
                .build();
    }

    public static Booking dtoToBooking(BookingDto dto) {
        Booking booking = new Booking();

        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(dto.getItem());
        booking.setRequestor(dto.getBooker());
        booking.setStatus(dto.getStatus());

        return booking;
    }
}
