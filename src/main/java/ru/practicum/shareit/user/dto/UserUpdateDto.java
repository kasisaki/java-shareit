package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserUpdateDto {
    private int id;
    private String login;
    private String name;
    private String email;
}
