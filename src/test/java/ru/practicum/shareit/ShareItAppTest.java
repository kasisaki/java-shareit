package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.IllegalStatusException;
import ru.practicum.shareit.exception.errorResponse.ErrorResponse;
import ru.practicum.shareit.utils.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.utils.BookingStatus.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ShareItAppTest {

    @Autowired
    ErrorHandler errorHandler;

    Constants constants = new Constants();

    @Test
    public void contextLoads() {
        assertTrue(true);
    }

    @Test
    public void testStatus() throws Exception {
        assertEquals(WAITING, ofString("WAITING"));
        assertEquals(APPROVED, ofString("APPROVED"));
        assertEquals(REJECTED, ofString("REJECTED"));
        assertEquals(CANCELED, ofString("CANCELED"));
    }

    @Test
    public void testErrorHandler() {
        ResponseEntity<ErrorResponse> response;
        response = errorHandler.catchPSQLException(new PSQLException(new ServerErrorMessage("error")));
        assertEquals(409, response.getStatusCodeValue());

        response = errorHandler.catchIllegalStatusException(new IllegalStatusException("error"));
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testConstants() throws Exception {
        assertEquals(constants.getSharerUserId(), "X-Sharer-User-Id");
    }


}
