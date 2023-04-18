package ru.practicum.shareit.features.user.service;

import ru.practicum.shareit.exceptions.DuplicationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.user.model.User;

import java.util.List;

public interface UserService {

    User postUser(User user) throws ValidationException, DuplicationException;

    User patchUser(Long userId, User user) throws DuplicationException, NotFoundException, ValidationException;

    User getUserById(Long userId) throws NotFoundException;

    List<User> getAllUser();

    void deleteUser(Long userId);
}
