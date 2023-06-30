package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.IllegalStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.CommonData.*;
import static ru.practicum.shareit.utils.BookingStatus.*;
import static ru.practicum.shareit.utils.DateUtils.now;

@SpringBootTest
public class BookingServiceTests {
    @Autowired
    BookingService bookingService;
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    Item itemMock;
    @MockBean
    User userMock;
    @MockBean
    BookingDto bookingDtoMock;
    @MockBean
    Booking booking;

    @Test
    public void testCreate() throws Exception {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemAvailable));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToUpdate));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);


        BookingDto testDto = bookingService.create(bookingDto1, 22L);

        assertNotNull(testDto.getId());
    }

    @Test
    @Rollback
    public void testCreateEndBeforeStart() throws Exception {
        bookingDtoUpdate.setEnd(now().plusMinutes(1));
        bookingDtoUpdate.setStart(now().plusMinutes(2));

        Exception e = assertThrows(BadRequestException.class, () -> bookingService.create(bookingDtoUpdate, 22L));
    }

    @Test
    public void testCreateNotAvailable() throws Exception {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemNotAvailable));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToUpdate));


        bookingDtoUpdate.setItem(itemNotAvailable);
        Exception e = assertThrows(BadRequestException.class, () -> bookingService.create(bookingDtoUpdate, 22L));
    }

    @Test
    public void testCreateBookingOfOwnItemError() throws Exception {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemAvailable));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToUpdate));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        Exception e = assertThrows(ElementNotFoundException.class, () -> bookingService.create(bookingDto1, 11L));
        assertEquals("Cannot book your own item", e.getMessage());
    }

    @Test
    public void testApproveBookingByNotRelatedUser() throws Exception {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        Exception e = assertThrows(BadRequestException.class,
                () -> bookingService.approveBooking(1L, true, 1L));
        assertEquals("Not allowed", e.getMessage());
    }

    @Test
    public void testApproveBookingByRequestor() throws Exception {
        Booking bookTest = new Booking();
        bookTest.setStatus(WAITING);
        bookTest.setRequestor(user1);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookTest));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        Exception e = assertThrows(ElementNotFoundException.class,
                () -> bookingService.approveBooking(1L, true, 11L));
        assertEquals("Requester cannot approve requested booking", e.getMessage());
    }

    @Test
    public void testApproveBookingAlreadyApproved() throws Exception {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingApproved));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToUpdate));

        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        Exception e = assertThrows(BadRequestException.class,
                () -> bookingService.approveBooking(1L, true, 11L));
        assertEquals("Already APPROVED", e.getMessage());
    }

    @Test
    public void testApproveBooking() throws Exception {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        BookingDto testDto = bookingService.approveBooking(1L, true, 11L);
        assertEquals(APPROVED, testDto.getStatus());
    }

    @Test
    public void testApproveBookingReject() throws Exception {
        Booking bookTest = new Booking();
        bookTest.setStatus(WAITING);
        bookTest.setRequestor(user2);
        bookTest.setItem(itemAvailable);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookTest));
        when(bookingRepository.save(any())).thenReturn(bookingRejected);
        when(booking.getStatus()).thenReturn(WAITING);

        BookingDto testDto = bookingService.approveBooking(1L, false, 11L);
        assertEquals(REJECTED, testDto.getStatus());
    }

    @Test
    public void testGetBookingRequestorNotExists() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ElementNotFoundException.class, () -> bookingService.getBooking(1L, 1L));
    }

    @Test
    public void testGetBookingRequestThrowUserNotExists() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ElementNotFoundException.class, () -> bookingService.getBooking(1L, 1L));

    }

    @Test
    public void testGetBookingRequestNoBookings() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.existsByIdAndRequestorId(anyLong(), anyLong())).thenReturn(false);
        when(bookingRepository.existsByIdAndItemOwnerId(anyLong(), anyLong())).thenReturn(false);

        Exception e = assertThrows(ElementNotFoundException.class, () -> bookingService.getBooking(1L, 1L));
        assertEquals("This user does not have requested booking", e.getMessage());
    }

    @Test
    public void testGetBookingRequest() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.existsByIdAndRequestorId(anyLong(), anyLong())).thenReturn(true);
        when(bookingRepository.existsByIdAndItemOwnerId(anyLong(), anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingApproved));

        BookingDto dtoTest = bookingService.getBooking(1L, 1L);

        assertEquals(dtoTest.getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserBookingsStateThrow() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ElementNotFoundException.class, () -> bookingService.getUserBookingsState(1L,
                "sds", 1, 1));
    }

    @Test
    public void testGetUserItemsStateThrow() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ElementNotFoundException.class, () -> bookingService.getUserItemsState(1L,
                "sds", 1, 1));
    }

    @Test
    public void testGetUserBookingsStateThrowIllegalState() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        assertThrows(IllegalStatusException.class, () -> bookingService.getUserBookingsState(1L,
                "sds", 1, 1));
    }

    @Test
    public void testGetUserItemsStateThrowIllegalState() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Exception e = assertThrows(IllegalStatusException.class, () -> bookingService.getUserItemsState(1L,
                "sds", 1, 1));
        assertEquals("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }

    @Test
    public void testGetUserBookingsStateCURRENT() throws Exception {
        when(bookingRepository
                .findAllByRequestorIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserBookingsState(1L, "CURRENT", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserBookingsStateFUTURE() throws Exception {
        when(bookingRepository
                .findAllByRequestorIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserBookingsState(1L, "FUTURE", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserBookingsStatePAST() throws Exception {
        when(bookingRepository
                .findAllByRequestorIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserBookingsState(1L, "PAST", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserBookingsStateREJECTED() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository
                .findAllByRequestorIdAndStatusIsOrderByStartDesc(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));

        List<BookingDto> dtoList = bookingService.getUserBookingsState(1L, "REJECTED", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserBookingsStateWAITING() throws Exception {
        when(bookingRepository
                .findAllByRequestorIdAndStatusIsOrderByStartDesc(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserBookingsState(1L, "WAITING", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserBookingsStateALL() throws Exception {
        when(bookingRepository
                .findAllByRequestorIdOrderByStartDesc(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserBookingsState(1L, "ALL", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserItemsStateALL() throws Exception {
        when(bookingRepository
                .findAllByItemOwnerIdOrderByStartDesc(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserItemsState(1L, "ALL", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserItemsStateCURRENT() throws Exception {
        when(bookingRepository
                .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserItemsState(1L, "CURRENT", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserItemsStatFUTURE() throws Exception {
        when(bookingRepository
                .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserItemsState(1L, "FUTURE", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserItemsStatePAST() throws Exception {
        when(bookingRepository
                .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserItemsState(1L, "PAST", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserItemsStateREJECTED() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));

        List<BookingDto> dtoList = bookingService.getUserItemsState(1L, "REJECTED", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }

    @Test
    public void testGetUserItemsStateWAITING() throws Exception {
        when(bookingRepository
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(bookingApproved)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<BookingDto> dtoList = bookingService.getUserItemsState(1L, "WAITING", 1, 1);
        assertEquals(1, dtoList.size());
        assertEquals(dtoList.get(0).getId(), bookingApproved.getId());
    }
}
