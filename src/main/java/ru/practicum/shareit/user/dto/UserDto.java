package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.customAnnotation.IsNotMatching;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank(message = "Login must not be empty")
    @IsNotMatching(matchValue = ".*\\s+.*", message = "Login must not contain spaces")
    private String login;
    private String name;
    @Email
    private String email;
}
