package ru.practicum.shareit.exception.errorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private int statusCode;
    private String error;
}
