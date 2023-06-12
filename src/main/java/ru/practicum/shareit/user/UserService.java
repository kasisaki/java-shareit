package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<UserDto> findAll() {
        List<User> usersList = userStorage.findAll();
        ArrayList<UserDto> usersDtoList = new ArrayList<>();
        for (User user : usersList) {
            usersDtoList.add(UserMapper.toItemDto(user));
        }
        return usersDtoList;
    }

    public UserDto getUser(int userId) {
        User user = userStorage.getUser(userId);

        if (user != null) {
            return UserMapper.toItemDto(user);
        }
        throw new ElementNotFoundException("User with userId " + userId + " not found");
    }

    public UserDto create(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            //user.setName(user.getLogin());
            log.warn("Name is not provided and set to match login");
        }
        return UserMapper.toItemDto(userStorage.create(user));
    }

    public UserDto update(User user, Integer id) {
        return UserMapper.toItemDto(userStorage.update(user, id));
    }

    public String delete(int userId) {
        userStorage.remove(userId);
        return "User with id " + userId + " deleted successfully";
    }
}
