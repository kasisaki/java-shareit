package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemComment;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTests {
    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private ItemService itemService;

    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private ItemUpdateDto itemUpdateDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        item = new Item();
        item.setId(1L);

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemUpdateDto = ItemUpdateDto.builder().build();
    }

    @Test
    void createTest() {
        itemUpdateDto.setAvailable(true);
        itemUpdateDto.setRequestId(1L);

        item = new Item();
        item.setName("test name");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));

        when(itemRepository.save(any())).thenReturn(item);

        ItemDto responseDto = itemService.create(itemUpdateDto, 1L);
        assertNotNull(responseDto);
    }


    @Test
    void createWithCommentTest() {
        CommentDto commentRequestDto = new CommentDto();
        commentRequestDto.setText("Test comment");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItemIdAndRequestorIdAndStartIsBefore(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(true);

        when(commentRepository.save(any(ItemComment.class))).thenReturn(new ItemComment());

        CommentDto responseDto = itemService.createItemComment(commentRequestDto, 1L, 1L);
        assertNotNull(responseDto);
    }

    @Test
    void createWithCommentUserTest() {
        CommentDto commentRequestDto = new CommentDto();
        commentRequestDto.setText("Test comment");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItemIdAndRequestorIdAndStartIsBefore(anyLong(), anyLong(),
                any(LocalDateTime.class)))
                .thenReturn(false);
        when(commentRepository.save(any(ItemComment.class))).thenReturn(new ItemComment());

        assertThrows(BadRequestException.class, () -> itemService.createItemComment(commentRequestDto, 1L, 1L));
    }

    @Test
    void updateTestWhenUserIdNotValidThrowException() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ElementNotFoundException.class, () -> itemService
                .updateItem(1L, ItemUpdateDto.builder().build(), 1L));
    }


    @Test
    void updateTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        item.setOwner(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto responseDto = itemService.updateItem(1L, ItemUpdateDto.builder().build(), 1L);
        assertNotNull(responseDto);
    }

    @Test
    void getByIdTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        item.setOwner(user);

        ItemDto responseDto = itemService.getItem(1L, 1L);
        assertNotNull(responseDto);
    }

    @Test
    void getAllByUserIdTest() {
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong())).thenReturn(Collections.singletonList(item));
        when(commentRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());
        item.setOwner(user);

        List<ItemDto> responseDto = itemService.getItemsOfOwner(1L);
        assertNotNull(responseDto);
        assertFalse(responseDto.isEmpty());
    }

    @Test
    void searchItemsTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableTrue(anyString())).thenReturn(Collections.singletonList(item));

        List<ItemDto> responseDto = itemService.searchItems("test");
        assertNotNull(responseDto);
        assertFalse(responseDto.isEmpty());
    }
}