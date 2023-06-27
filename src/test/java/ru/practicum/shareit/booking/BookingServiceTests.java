package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.CommonData.*;
import static ru.practicum.shareit.utils.BookingStatus.APPROVED;
import static ru.practicum.shareit.utils.BookingStatus.REJECTED;
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
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        Exception e = assertThrows(ElementNotFoundException.class, () -> bookingService.create(bookingDto1, 11L));
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
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        Exception e = assertThrows(ElementNotFoundException.class,
                () -> bookingService.approveBooking(1L, true, 11L));
        assertEquals("Requester cannot approve requested booking", e.getMessage());
    }

    @Test
    public void testApproveBookingAlreadyApproved() throws Exception {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingApproved));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        Exception e = assertThrows(BadRequestException.class,
                () -> bookingService.approveBooking(1L, true, 11L));
        assertEquals("Already APPROVED", e.getMessage());
    }

    @Test
    public void testApproveBooking() throws Exception {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        BookingDto testDto = bookingService.approveBooking(1L, true, 22L);
        assertEquals(APPROVED, testDto.getStatus());
    }

    @Test
    public void testApproveBookingReject() throws Exception {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any())).thenReturn(bookingRejected);

        BookingDto testDto = bookingService.approveBooking(1L, false, 22L);
        assertEquals(REJECTED, testDto.getStatus());
    }

    @Test
    public void testGetBookingRequestorNotExists() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ElementNotFoundException.class, () -> bookingService.getBooking(1L, 1L));
    }

    @Test
    public void testCreate6() throws Exception {

    }
    @Test
    public void testCreate5() throws Exception {

    }@Test
    public void testCreate4() throws Exception {

    }@Test
    public void testCreate3() throws Exception {

    }@Test
    public void testCreate2() throws Exception {

    }
}
