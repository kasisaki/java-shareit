package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingService;

@WebMvcTest(controllers = ItemMapperTests.class)
@AutoConfigureMockMvc
public class ItemControllerTests {
    private final String url = "/items";
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService itemsService;
    @Autowired
    private MockMvc mvc;
}
