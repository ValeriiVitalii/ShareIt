package ru.practicum.features.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    UserClient userClient;


    @PostMapping
    public ResponseEntity<Object> postUser(@Valid @RequestBody User user) {
        return userClient.postUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUser(@PathVariable("userId") Long userId, @RequestBody User user) {

        return userClient.patchUser(userId, user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") Long userId) {
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser() {
        return userClient.getAllUser();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userClient.deleteUser(userId);
    }
}
