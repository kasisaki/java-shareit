package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

import static ru.practicum.shareit.booking.BookingMapper.bookingToDtoShort;
import static ru.practicum.shareit.booking.BookingMapper.dtoToBooking;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.utils.BookingStatus.*;
import static ru.practicum.shareit.utils.DateUtils.now;

public class CommonData {
    public static final User user1 = new User(
            11L,
            "userName",
            "userEmail@email.com"
    );

    public static final UserUpdateDto userDtoJustEmail = UserUpdateDto.builder()
            .email("dtoJustEmail@email.com")
            .build();

    public static final UserUpdateDto userDtoJustName = UserUpdateDto.builder()
            .name("dtoJustName")
            .build();

    public static final ItemRequest request = ItemRequest.builder()
            .id(55L)
            .requester(user1)
            .build();

    public static final ItemUpdateDto itemFromBody = ItemUpdateDto.builder()
            .id(222L)
            .name("itemName")
            .description("itemDescription")
            .available(true)
            .owner(user1)
            .request(request)
            .build();
    public static ItemUpdateDto wrongItem = ItemUpdateDto.builder()
            .id(itemFromBody.getId())
            .name(itemFromBody.getName())
            .owner(itemFromBody.getOwner())
            .request(itemFromBody.getRequest())
            .build();
    public static final Item item = new Item(
            222L,
            "itemName",
            "itemDescription",
            true,
            user1,
            55L
    );
    public static final Booking bookingRejected = new Booking(
            333L,
            now().minusDays(3),
            now().plusDays(3),
            item,
            user1,
            REJECTED
    );
    public static final Booking bookingApproved = new Booking(
            444L,
            now().minusDays(2),
            now().plusDays(2),
            item,
            user1,
            APPROVED
    );

    public static final BookingDto bookingDto2 = BookingDto.builder()
            .id(2L)
            .start(now().plusHours(1))
            .end(now().plusHours(2))
            .itemId(555L)
            .item(new Item())
            .booker(new User())
            .status(APPROVED)
            .build();

    public static final BookingDto bookingDto1 = BookingDto.builder()
            .id(1L)
            .start(now().plusHours(1))
            .end(now().plusHours(2))
            .itemId(222L)
            .item(item)
            .booker(user1)
            .status(WAITING)
            .build();

    public static final ItemDto itemDto = toItemDto(
            item,
            bookingToDtoShort(dtoToBooking(bookingDto1)),
            bookingToDtoShort(dtoToBooking(bookingDto2)),
            new ArrayList<>());

    public static final CommentDto commentDto = new CommentDto(
            77L,
            "commentText",
            user1.getName(),
            now()
    );
}
