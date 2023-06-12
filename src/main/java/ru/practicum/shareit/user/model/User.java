package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Data
@Builder
public class User {
    private int id;
    //@NotBlank(message = "Login must not be empty")
    //@IsNotMatching(matchValue = ".*\\s+.*", message = "Login must not contain spaces")
    private String login;
    private String name;
    @Email
    @NotEmpty
    private String  email;
}
