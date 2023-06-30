package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTests {
    private final String url = "/users";
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .login("userLogin")
            .name("userName")
            .email("userEmail@email.com")
            .build();
    private final UserDto userDto5 = UserDto.builder()
            .id(5L)
            .login("user5Login")
            .name("user5Name")
            .email("user5Email@email.com")
            .build();
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mvc;

    @Test
    void testCreateUser() throws Exception {
        when(userService.create(any()))
                .thenReturn(userDto);

        mvc.perform(post(url)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void testUpdateUser() throws Exception {
        String urlPath = url + "/{id}";
        when(userService.update(any(), anyLong()))
                .thenReturn(userDto);

        mvc.perform(patch(urlPath, 1L)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void testDeleteUser() throws Exception {
        String urlPath = url + "/{id}";
        when(userService.delete(anyLong()))
                .thenReturn("Deleted");

        mvc.perform(delete(urlPath, 1L)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted"));
    }

    @Test
    void testFindAllUsersWhen2UsersFound() throws Exception {
        List<UserDto> usersDtoList = List.of(userDto, userDto5);
        when(userService.findAll())
                .thenReturn(usersDtoList);

        mvc.perform(get(url)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(5))
                .andExpect(jsonPath("$[1].email").value("user5Email@email.com"));
    }

    @Test
    void testFindAllUsersWhenNoUsersFound() throws Exception {
        List<UserDto> usersDtoList = new ArrayList<>();
        when(userService.findAll())
                .thenReturn(usersDtoList);

        mvc.perform(get(url)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetUserUserExists() throws Exception {
        String urlPath = url + "/{id}";
        when(userService.getUser(anyLong()))
                .thenReturn(userDto);

        mvc.perform(get(urlPath, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void testGetUserNotFound() throws Exception {
        String urlPath = url + "/{id}";
        when(userService.getUser(anyLong()))
                .thenThrow(new ElementNotFoundException("User not found"));

        mvc.perform(get(urlPath, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(jsonPath("$.login").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist());
    }

    @Test
    void testGetUserConflict() throws Exception {
        String urlPath = url + "/{id}";
        when(userService.getUser(anyLong()))
                .thenThrow(new ConflictException("conflict"));

        mvc.perform(get(urlPath, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(jsonPath("$.login").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist());
    }
}
