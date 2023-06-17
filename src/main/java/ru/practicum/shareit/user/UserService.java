package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<UserDto> findAll() {
        return userStorage.findAll().stream().map(UserMapper::toItemDto).collect(Collectors.toList());
    }

    public UserDto getUser(int userId) {
        User user = userStorage.getUser(userId);

        if (user != null) {
            return UserMapper.toItemDto(user);
        }
        throw new ElementNotFoundException("User with userId " + userId + " not found");
    }

    public UserDto create(UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getName() == null || userUpdateDto.getName().equals("")) {
            log.warn("Name is not provided and set to match login");
        }
        if (userUpdateDto.getEmail() == null || !userUpdateDto.getEmail().contains("@")) {
            throw new BadRequestException("Email is missing or in wrong format");
        }
        return UserMapper.toItemDto(userStorage.create(userUpdateDto));
    }

    public UserDto update(UserUpdateDto userUpdateDto, Integer id) {
        return UserMapper.toItemDto(userStorage.update(userUpdateDto, id));
    }

    public String delete(int userId) {
        userStorage.remove(userId);
        return "User with id " + userId + " deleted successfully";
    }
}
