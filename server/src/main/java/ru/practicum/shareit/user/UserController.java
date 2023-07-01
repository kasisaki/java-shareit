package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(required = false) long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserUpdateDto userUpdateDto) {
        return new ResponseEntity<>(userService.create(userUpdateDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable long id,
                                              @RequestBody UserUpdateDto userUpdateDto) {
        return new ResponseEntity<>(userService.update(userUpdateDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
