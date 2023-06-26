package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalStatusException extends RuntimeException {
    public IllegalStatusException() {
        super();
    }

    public IllegalStatusException(String message) {
        super(message);
    }

    public IllegalStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalStatusException(Throwable cause) {
        super(cause);
    }

    protected IllegalStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
