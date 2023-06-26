package ru.practicum.shareit.user.mapper;


import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User updateUserFromDto(User user, UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        return user;
    }
}
