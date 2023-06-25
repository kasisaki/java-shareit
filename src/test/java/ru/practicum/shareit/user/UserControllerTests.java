package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {
    private final String url = "/users";
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mvc;
    private UserDto userDto = UserDto.builder()
            .id(1L)
            .login("userLogin")
            .name("userName")
            .email("userEmail@email.com")
            .build();

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
    void testFindAllUsers() throws Exception {
        List<UserDto> usersDtoList = List.of(userDto, userDto);
        when(userService.findAll())
                .thenReturn(usersDtoList);

        mvc.perform(delete(url)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.data").le("Deleted"));
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(required = false) long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

}
