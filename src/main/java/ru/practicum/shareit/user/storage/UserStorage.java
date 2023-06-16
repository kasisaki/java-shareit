package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User create(UserUpdateDto userUpdateDto);

    User getUser(Integer id);

    void remove(Integer userId);

    User update(UserUpdateDto userUpdateDto, Integer id);

    List<User> findAll();
}
