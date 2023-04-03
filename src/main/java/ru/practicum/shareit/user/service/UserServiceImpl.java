package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserService userService;

    @Override
    public User createUser(User user) throws ValidationException, DuplicationException {
        return userService.createUser(user);
    }

    @Override
    public User editUser(Long userId, User user) throws DuplicationException {
        return userService.editUser(userId, user);
    }

    @Override
    public User getUser(Long userId) throws NotFoundException {
        return userService.getUser(userId);
    }

    @Override
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @Override
    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }
}
