package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserUpdateDto {
    private String login;
    private Long id;
    private String name;
    private String email;
}
