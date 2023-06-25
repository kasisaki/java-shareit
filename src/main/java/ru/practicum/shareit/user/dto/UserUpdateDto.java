package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.customAnnotation.IsNotMatching;


@Getter
@Setter
@Builder
public class UserUpdateDto {
    @IsNotMatching(matchValue = ".*\\s+.*", message = "Login must not contain spaces")
    private String login;
    private Long id;
    private String name;
    private String email;
}
