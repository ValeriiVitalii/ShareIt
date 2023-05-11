package ru.practicum.shareit.features.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.DuplicationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.user.service.UserService;
import ru.practicum.shareit.features.user.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    UserService userService;


    @PostMapping
    public User postUser(@Valid @RequestBody User user) throws ValidationException, DuplicationException {
        return userService.postUser(user);
    }

    @PatchMapping("/{userId}")
    public User patchUser(@PathVariable("userId") Long userId, @RequestBody User user)
            throws ValidationException, DuplicationException, NotFoundException {

        return userService.patchUser(userId, user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) throws NotFoundException {
        return userService.getUserById(userId);
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
