package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("User with userId " + userId + " not found")));
    }

    public UserDto create(UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getName() == null || userUpdateDto.getName().equals("")) {
            log.warn("Name is not provided and set to match login");
        }
        User user = new User();
        return UserMapper.toUserDto(userRepository.save(UserMapper.updateUserFromDto(user, userUpdateDto)));
    }

    public UserDto update(UserUpdateDto userUpdateDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("User with userId " + userId + " not found"));

        return UserMapper.toUserDto(userRepository.save(UserMapper.updateUserFromDto(user, userUpdateDto)));
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
        log.info("User with userId " + userId + "deleted");
    }
}
