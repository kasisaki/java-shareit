package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.utils.BookingStatus.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ShareItAppTest {

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void testStatus() throws Exception {
        assertEquals(WAITING, ofString("WAITING"));
        assertEquals(APPROVED, ofString("APPROVED"));
        assertEquals(REJECTED, ofString("REJECTED"));
        assertEquals(CANCELED, ofString("CANCELED"));
    }

}
