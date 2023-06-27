package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.CommonData.*;
import static ru.practicum.shareit.booking.BookingMapper.bookingToDtoShort;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.updateItemWithDto;

@SpringBootTest
public class ItemMapperTests {
    private final Item itemToUpdate = new Item();
    @BeforeEach
    void  setup() throws Exception {
        itemToUpdate.setId(0L);
        itemToUpdate.setName("untouched Name");
        itemToUpdate.setDescription("untouched description");
        itemToUpdate.setAvailable(true);
        itemToUpdate.setOwner(user1);
        itemToUpdate.setRequestId(999L);
    }

    @Test
    public void testToItemDto() throws Exception {
        ItemDto itemDto = toItemDto(itemAvailable,
                bookingToDtoShort(bookingApproved),
                bookingToDtoShort(bookingApproved),
                new ArrayList<>() );
        assertEquals(itemDto.getName(), itemAvailable.getName());
        assertEquals(itemDto.getLastBooking(), bookingToDtoShort(bookingApproved));
        assertEquals(itemDto.getDescription(), itemAvailable.getDescription());
    }

    @Test
    public void testToItemDtoNull() throws Exception {
        assertNull(toItemDto(null,
                bookingToDtoShort(bookingRejected),
                bookingToDtoShort(bookingApproved),
                new ArrayList<>()));
    }

    @Test
    public void testUpdateItemWithDtoItemIsNullExpectNull() throws Exception {
        assertNull(updateItemWithDto(null, itemFromBody));
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
