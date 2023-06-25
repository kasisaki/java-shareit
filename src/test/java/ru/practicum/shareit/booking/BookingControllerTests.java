package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.BookingStatus.APPROVED;
import static ru.practicum.shareit.utils.BookingStatus.WAITING;
import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;
import static ru.practicum.shareit.utils.DateUtils.now;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTests {
    private final String url = "/bookings";
    private final User user = new User(
            11L,
            "userName",
            "userEmail@email.com"
    );
    private final Item item = new Item(
            222L,
            "itemName",
            "itemDescription",
            true,
            user,
            55L
    );
    private final BookingDto bookingDto1 = BookingDto.builder()
            .id(1L)
            .start(now().plusHours(1))
            .end(now().plusHours(2))
            .itemId(222L)
            .item(item)
            .booker(user)
            .status(WAITING)
            .build();
    private final BookingDto bookingDto2 = BookingDto.builder()
            .id(2L)
            .start(now().plusHours(1))
            .end(now().plusHours(2))
            .itemId(1L)
            .item(new Item())
            .booker(new User())
            .status(APPROVED)
            .build();
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    @Test
    void testCreateBooking() throws Exception {
        when(bookingService.create(any(), anyLong()))
                .thenReturn(bookingDto1);

        mvc.perform(post(url)
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 11))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(WAITING.toString())))
                .andExpect(jsonPath("$.itemId", is(bookingDto1.getItemId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class));
    }


    @Test
    void testGetBookingExists() throws Exception {
        String urlPath = url + "/{bookingId}";
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto1);

        mvc.perform(get(urlPath, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 11))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(WAITING.toString())))
                .andExpect(jsonPath("$.itemId", is(bookingDto1.getItemId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class));
    }

    @Test
    void testApproveBookingCorrect() throws Exception {
        String urlPath = url + "/{bookingId}";
        when(bookingService.approveBooking(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingDto1);

        mvc.perform(patch(urlPath, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 11)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(WAITING.toString())))
                .andExpect(jsonPath("$.itemId", is(bookingDto1.getItemId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class));
        bookingDto1.setStatus(WAITING);
    }

    @Test
    void tesGetUserBookingsState() throws Exception {
        List<BookingDto> bookingList = List.of(bookingDto1, bookingDto2);
        when(bookingService.getUserBookingsState(anyLong(), any()))
                .thenReturn(bookingList);

        mvc.perform(get(url)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 11)
                        .param("state", "CURRENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto1.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(WAITING.toString())))
                .andExpect(jsonPath("$[0].itemId", is(bookingDto1.getItemId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(user.getId()), Long.class));
        bookingDto1.setStatus(WAITING);
    }

    @Test
    void tesGetUserItemsState() throws Exception {
        String urlPath = url + "/owner";
        List<BookingDto> bookingList = List.of(bookingDto1, bookingDto2);
        when(bookingService.getUserItemsState(anyLong(), any()))
                .thenReturn(bookingList);

        mvc.perform(get(urlPath)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 11)
                        .param("state", "CURRENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto1.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(WAITING.toString())))
                .andExpect(jsonPath("$[0].itemId", is(bookingDto1.getItemId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(bookingDto2.getId()), Long.class));
        bookingDto1.setStatus(WAITING);
    }


    @Test
    void testGetBookingError() throws Exception {
        String urlPath = url + "/{bookingId}";
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenThrow(new ElementNotFoundException("Booking not found"));

        mvc.perform(get(urlPath, 11)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 11))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.start").doesNotExist())
                .andExpect(jsonPath("$.status").doesNotExist())
                .andExpect(jsonPath("$.item.itemId").doesNotExist());
    }

    @Test
    void testGetBookingErrorNoHeader() throws Exception {
        String urlPath = url + "/{bookingId}";
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto1);

        mvc.perform(get(urlPath, 11)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.start").doesNotExist())
                .andExpect(jsonPath("$.status").doesNotExist())
                .andExpect(jsonPath("$.item.itemId").doesNotExist());
    }


}
