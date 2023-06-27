package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.CommonData;
import ru.practicum.shareit.item.model.ItemComment;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.shareit.CommonData.*;
import static ru.practicum.shareit.booking.BookingMapper.bookingToDtoShort;
import static ru.practicum.shareit.item.mapper.CommentMapper.mapCommentRequestDtoToComment;
import static ru.practicum.shareit.item.mapper.CommentMapper.mapCommentToCommentResponseDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.updateItemWithDto;
import static ru.practicum.shareit.utils.DateUtils.now;

@SpringBootTest
public class ItemCommentMapperTests {
    private final ItemComment itemCommentToUpdate = new ItemComment();
    @BeforeEach
    void  setup() throws Exception {
        itemCommentToUpdate.setId(0L);
        itemCommentToUpdate.setText("untouched text");
        itemCommentToUpdate.setItem(item);
        itemCommentToUpdate.setAuthor(user1);
        itemCommentToUpdate.setCreated(now().minusHours(5));
    }

    @Test
    public void testToItemDto() throws Exception {
        ItemComment comment = mapCommentRequestDtoToComment(CommonData.commentDto, item, user1);
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getLastBooking(), bookingToDtoShort(bookingApproved));
        assertEquals(itemDto.getDescription(), item.getDescription());
    }

    @Test
    public void testToItemDtoNull() throws Exception {
        assertNull(toItemDto(null,
                bookingToDtoShort(bookingRejected),
                bookingToDtoShort(bookingApproved),
                new ArrayList<>()));
    }

    @Test
    public void testmapCommentToCommentResponseDtoNullExpectNull() throws Exception {
        assertNull(mapCommentToCommentResponseDto(null));
    }

    @Test
    public void testUpdateItemWithDtoNullExpectItemUnchanged() throws Exception {
        assertNotEquals(itemToUpdate.getName(), itemFromBody.getName());
        assertNotEquals(itemToUpdate.getDescription(), itemFromBody.getDescription());
        assertNotEquals(itemToUpdate.getRequestId(), itemFromBody.getRequestId());
        updateItemWithDto(itemToUpdate, itemFromBody);
        assertEquals(itemToUpdate.getName(), itemFromBody.getName());
        assertEquals(itemToUpdate.getDescription(), itemFromBody.getDescription());
    }

    @Test
    public void testUpdateItemWithDtoWithNullNameExpectUnchanged() throws Exception {
        assertNotEquals(itemToUpdate.getName(), itemFromBody.getName());
        assertNotEquals(itemToUpdate.getDescription(), itemFromBody.getDescription());
        assertNotEquals(itemToUpdate.getRequestId(), itemFromBody.getRequestId());
        updateItemWithDto(itemToUpdate, null);
        assertNotEquals(itemToUpdate.getName(), itemFromBody.getName());
        assertNotEquals(itemToUpdate.getDescription(), itemFromBody.getDescription());
    }

    @Test
    public void testUpdateItemWithDtoWithEmptyNameExpectUnchangedName() throws Exception {
        assertNotEquals(itemToUpdate.getName(), itemFromBody.getName());
        assertNotEquals(itemToUpdate.getDescription(), itemFromBody.getDescription());
        assertNotEquals(itemToUpdate.getRequestId(), itemFromBody.getRequestId());
        itemFromBody.setName("");
        updateItemWithDto(itemToUpdate, itemFromBody);
        assertNotEquals(itemToUpdate.getName(), itemFromBody.getName());
        itemFromBody.setName("itemFromBodyName");
    }
}
