package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.DuplicationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping
    public User postUser(@Valid @RequestBody User user) throws ValidationException, DuplicationException {
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public User patchUser(@PathVariable("userId") Long userId, @Valid @RequestBody User user) throws DuplicationException {
        return userService.editUser(userId, user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId) throws NotFoundException {
        return userService.getUser(userId);
    }

    @GetMapping
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }
}
