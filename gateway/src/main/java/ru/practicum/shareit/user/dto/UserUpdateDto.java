package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@Builder
public class UserUpdateDto {
    private String login;
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
}
