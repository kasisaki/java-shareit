package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.CommonData.*;
import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTests {
    private final String url = "/items";
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService itemService;
    @Autowired
    private MockMvc mvc;

    @BeforeAll
    static void before() {
        itemDto.getLastBooking().setId(bookingDto1.getId());
        itemDto.getNextBooking().setId(bookingDto2.getId());
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.create(any(), anyLong()))
                .thenReturn(itemDto);


        mvc.perform(post(url)
                        .content(mapper.writeValueAsString(itemFromBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.id", is(bookingDto1.getItem().getId()), Long.class));
    }

    @Test
    void testCreateItemFail() throws Exception {
        when(itemService.create(any(), anyLong()))
                .thenThrow(new BadRequestException("BadRequest"));


        mvc.perform(post(url)
                        .content(mapper.writeValueAsString(itemFromBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.lastBooking.id").doesNotExist())
                .andExpect(jsonPath("$.nextBooking.id").doesNotExist())
                .andExpect(jsonPath("$.available").doesNotExist())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void testCreateComment() throws Exception {
        String urlPath = url + "/{itemId}/comment";
        when(itemService.createItemComment(any(), anyLong(), anyLong()))
                .thenReturn(commentDto);


        mvc.perform(post(urlPath, "1")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }


    @Test
    void testUpdateItem() throws Exception {
        String urlPath = url + "/{itemId}";
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(itemDto);


        mvc.perform(patch(urlPath, 22L)
                        .content(mapper.writeValueAsString(itemFromBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.id", is(bookingDto1.getItem().getId()), Long.class));
    }

    @Test
    void testUpdateItemFailNoHeader() throws Exception {
        String urlPath = url + "/{itemId}";
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(itemDto);


        mvc.perform(patch(urlPath, 222)
                        .content(mapper.writeValueAsString(wrongDtoItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.lastBooking.id").doesNotExist())
                .andExpect(jsonPath("$.nextBooking.id").doesNotExist())
                .andExpect(jsonPath("$.available").doesNotExist())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void testGetAllItems() throws Exception {
        List<ItemDto> itemList = List.of(itemDto, itemDto);
        when(itemService.getItemsOfOwner(anyLong()))
                .thenReturn(itemList);


        mvc.perform(get(url)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[1].id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    void testGetItem() throws Exception {
        String urlPath = url + "/{itemId}";
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemDto);


        mvc.perform(get(urlPath, 22L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.id", is(bookingDto1.getItem().getId()), Long.class));
    }

    @Test
    void testGetItemNoHeader() throws Exception {
        String urlPath = url + "/{itemId}";
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemDto);


        mvc.perform(get(urlPath, 22L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.lastBooking.id").doesNotExist())
                .andExpect(jsonPath("$.nextBooking.id").doesNotExist())
                .andExpect(jsonPath("$.available").doesNotExist())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void testGetItemUserNotFound() throws Exception {
        String urlPath = url + "/{itemId}";
        when(itemService.getItem(anyLong(), anyLong()))
                .thenThrow(new ElementNotFoundException("User not found"));


        mvc.perform(get(urlPath, 22L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.lastBooking.id").doesNotExist())
                .andExpect(jsonPath("$.nextBooking.id").doesNotExist())
                .andExpect(jsonPath("$.available").doesNotExist())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void testSearchItem() throws Exception {
        String urlPath = url + "/search";
        List<ItemDto> itemList = List.of(itemDto, itemDto);
        when(itemService.searchItems(anyString()))
                .thenReturn(itemList);


        mvc.perform(get(urlPath)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1)
                        .param("text", "search content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[1].id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    void testSearchItemEmptySearchContent() throws Exception {
        String urlPath = url + "/search";
        when(itemService.searchItems(anyString()))
                .thenReturn(new ArrayList<>());


        mvc.perform(get(urlPath)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_USER_ID, 1)
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].lastBooking.id").doesNotExist())
                .andExpect(jsonPath("$[0].nextBooking.id").doesNotExist())
                .andExpect(jsonPath("$[0].available").doesNotExist())
                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[2].id").doesNotExist());
    }
}
