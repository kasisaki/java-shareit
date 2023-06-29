package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.shareit.CommonData.user1;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.dtoToItemRequest;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.requestToItemRequestDto;

@SpringBootTest
public class RequestMapperTests {

    @Test
    public void testRequestToItemRequestDto() throws Exception {
        assertNull(requestToItemRequestDto(null));
    }

    @Test
    public void testDtoToItemRequest() throws Exception {
        assertNull(dtoToItemRequest(null, user1));
    }
}
