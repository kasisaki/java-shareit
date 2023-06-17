package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.errorResponse.ErrorResponse;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchBadRequestException(final BadRequestException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(BAD_REQUEST.value(), e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchNotFoundException(final ElementNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(NOT_FOUND.value(), e.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchConflictException(final ConflictException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(CONFLICT.value(), e.getMessage()), CONFLICT);
    }
}