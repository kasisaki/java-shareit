package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserUpdateDto {
    private String login;
    private Long id;
    private String name;
    private String email;
}
