package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User getUser(Integer id);

    void remove(Integer userId);

    User update(User user, Integer id);

    List<User> findAll();
}
