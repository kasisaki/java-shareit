package ru.practicum.shareit.user.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
@Data
public class UserMemoryStorage implements UserStorage {

    private final Map<Integer, User> usersById = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();
    //При сокращении до сета возникнут проблемы при попытке обновить почту юзера на другую, которая уже занята
    //по условию почта должна быть уникальной
    //Так я могу сравнить id юзеров и принять верное решение.

    private Integer id = 0;

    public User create(UserUpdateDto userUpdateDto) {
        User user = User.builder().build();
        if (usersByEmail.containsKey(userUpdateDto.getEmail())) {
            throw new ConflictException("User " + userUpdateDto.getEmail() + " already exists");
        }
        user.setId(++id);
        UserMapper.userUpdateFromDto(user, userUpdateDto);
        usersById.put(user.getId(), user);
        usersByEmail.put(user.getEmail(), user);
        return user;
    }

    @Override
    public User getUser(Integer id) {
        return usersById.get(id);
    }

    public void remove(Integer userId) {
        if (!usersById.containsKey(userId)) {
            throw new ElementNotFoundException("User " + userId + "not found");
        }
        usersByEmail.remove(usersById.get(userId).getEmail());
        usersById.remove(userId);
    }

    public User update(UserUpdateDto userUpdateDto, Integer id) {
        if (!usersById.containsKey(id)) {
            throw new ElementNotFoundException("User " + id + "not found");
        }
        if (usersByEmail.containsKey(userUpdateDto.getEmail())
                && usersByEmail.get(userUpdateDto.getEmail()).getId() != id) {
            throw new ConflictException("Duplicate emails not allowed!");
        }
        usersByEmail.remove(usersById.get(id).getEmail());
        UserMapper.userUpdateFromDto(usersById.get(id), userUpdateDto);
        usersByEmail.put(usersById.get(id).getEmail(), usersById.get(id));
        return usersById.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }
}
