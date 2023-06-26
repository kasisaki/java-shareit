package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.CommonData.*;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;
import static ru.practicum.shareit.user.mapper.UserMapper.updateUserFromDto;

@SpringBootTest
public class UserMapperTests {

    @Test
    public void testToUserDto() throws Exception {
        assertEquals(user1.getId(), toUserDto(user1).getId());
        assertEquals(user1.getName(), toUserDto(user1).getName());
        assertEquals(user1.getEmail(), toUserDto(user1).getEmail());
        assertNotSame(user1, toUserDto(user1));
    }

    @Test
    public void testUpdateUserFromDto() throws Exception {
        assertNotEquals(userDtoJustName.getName(), user1.getName());
        assertEquals(userDtoJustName.getName(), updateUserFromDto(user1, userDtoJustName).getName());
        assertEquals(userDtoJustEmail.getEmail(), updateUserFromDto(user1, userDtoJustEmail).getEmail());
        assertEquals(userDtoJustName.getName(), user1.getName());
    }

    @Test
    public void testToUserDtoNull() throws Exception {
        assertNull(toUserDto(null));
    }

    @Test
    public void testUpdateUserFromDtoUserNull() throws Exception {
        assertNull(updateUserFromDto(null, userDtoJustName));
    }

    @Test
    public void testUpdateUserFromDtoNull() throws Exception {
        assertEquals(updateUserFromDto(user1, null), user1);
    }
}
