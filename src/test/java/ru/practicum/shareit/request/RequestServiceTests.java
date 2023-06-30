package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RequestServiceTests {
    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRequestService itemRequestService;

    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private RequestDto itemRequestDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        item = new Item();
        item.setId(1L);

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequestDto = RequestDto.builder().build();
    }

    @Test
    public void createTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        RequestDto responseDto = itemRequestService.create(itemRequestDto, 1L);
        assertNotNull(responseDto);
    }

    @Test
    public void createTestUnknownUserThrow() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        assertThrows(ElementNotFoundException.class, () -> itemRequestService.getRequest(2L, 3L));
    }

    @Test
    public void getAllByUserTest() {
        item.setOwner(user);
        item.setRequestId(itemRequest.getId());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<ItemRequest> itemRequestList = new ArrayList<>(Collections.singletonList(itemRequest));
        when(itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(anyLong())).thenReturn(itemRequestList);
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(Collections.singletonList(item));

        List<RequestDto> responseDto = itemRequestService.getAllRequestsOfUser(1L);
        assertNotNull(responseDto);
        assertFalse(responseDto.isEmpty());
    }

    @Test
    public void getAllTest() {
        item.setOwner(user);
        item.setRequestId(itemRequest.getId());

        when(userRepository.existsById(anyLong())).thenReturn(true);
        Page<ItemRequest> itemRequestPage = new PageImpl<>(Collections.singletonList(itemRequest),
                PageRequest.of(0, 10), 1);
        when(itemRequestRepository.findAllByRequestorIdIsNotOrderByCreatedAsc(anyLong(), any()))
                .thenReturn(itemRequestPage);
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(Collections.singletonList(item));

        List<RequestDto> responseDto = itemRequestService.getAllRequests(1L, 0, 10);
        assertNotNull(responseDto);
        assertFalse(responseDto.isEmpty());
    }

    @Test
    public void getItemTest() {
        item.setOwner(user);
        item.setRequestId(itemRequest.getId());

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(Collections.singletonList(item));

        RequestDto responseDto = itemRequestService.getRequest(1L, 1L);
        assertNotNull(responseDto);
    }
}