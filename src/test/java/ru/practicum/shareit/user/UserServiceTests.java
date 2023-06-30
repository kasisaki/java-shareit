package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.CommonData.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserService userService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    UserMapper userMapper;

    @Test
    public void testFindAll() throws Exception {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());
        List<UserDto> userList = userService.findAll();

        assertNotNull(userList);
    }

    @Test
    public void getUsers() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        UserDto userDto = userService.getUser(anyLong());

        assertNotNull(userDto);
        assertEquals(userDto.getId(), user2.getId());
        assertEquals(userDto.getName(), user2.getName());
    }

    @Test
    public void testCreate() throws Exception {
        when(userRepository.save(any())).thenReturn(userToUpdate);

        UserDto userDto = userService.create(userDtoJustEmail);

        assertEquals(userDto.getName(), userToUpdate.getName());
    }

    @Test
    public void testUpdateError() throws Exception {
        when(userRepository.save(any())).thenReturn(userToUpdate);

        Exception e = assertThrows(ElementNotFoundException.class, () -> userService.update(userDtoJustEmail, 1L));
        assertEquals("User with userId 1 not found", e.getMessage());
    }

    @Test
    public void testUpdate() throws Exception {
        when(userRepository.save(any())).thenReturn(userToUpdate);
        when(userRepository.findById(any())).thenReturn(Optional.of(userToUpdate));

        UserDto tetsUserDto = userService.update(userDtoJustEmail, 1L);
        assertEquals(tetsUserDto.getEmail(), userDtoJustEmail.getEmail());
    }

    @Test
    public void testDelete() throws Exception {
        assertEquals("User with id 1 deleted successfully", userService.delete(1L));
    }
}
