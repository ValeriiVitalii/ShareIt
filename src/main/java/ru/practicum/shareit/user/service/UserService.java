package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.DuplicationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user) throws ValidationException, DuplicationException;

    User editUser(Long userId, User user) throws DuplicationException;

    User getUser(Long userId) throws NotFoundException;

    List<User> getAllUser();

    void deleteUser(Long userId);
}
