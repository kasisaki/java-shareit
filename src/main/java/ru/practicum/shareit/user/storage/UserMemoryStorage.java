package ru.practicum.shareit.user.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Data
public class UserMemoryStorage implements UserStorage {

    private final HashMap<Integer, User> usersById = new HashMap<>();
    private final HashMap<String, User> usersByEmail = new HashMap<>();
    //Такая реализация позволяет не проходиться по пользователям в поисках эл.почты
    //На этапе подключения БД уберу

    private Integer id = 0;

    public User create(User user) {
        if (usersByEmail.containsKey(user.getEmail())) {
            throw new ConflictException("User " + user.getEmail() + " already exists");
        }
        user.setId(++id);
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

    public User update(User user, Integer id) {
        if (!usersById.containsKey(id)) {
            throw new ElementNotFoundException("User " + id + "not found");
        }
        if (usersByEmail.containsKey(user.getEmail())
                && usersByEmail.get(user.getEmail()).getId() != id) {
            throw new ConflictException("Duplicate emails not allowed!");
        }
        usersByEmail.remove(usersById.get(id).getEmail());
        updateIfNotNull(user, id);
        usersByEmail.put(usersById.get(id).getEmail(), usersById.get(id));
        return usersById.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }

    private void updateIfNotNull(User user, Integer id) {
        if (user.getEmail() != null) {
            usersById.get(id).setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            usersById.get(id).setName(user.getName());
        }
        if (user.getLogin() != null) {
            usersById.get(id).setLogin(user.getLogin());
        }
    }
}
