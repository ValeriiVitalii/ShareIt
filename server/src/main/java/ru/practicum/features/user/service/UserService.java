package ru.practicum.features.user.service;


import ru.practicum.features.user.model.User;
import ru.practicum.exceptions.*;

import java.util.List;

public interface UserService {

    User postUser(User user) throws ValidationException, DuplicationException;

    User patchUser(Long userId, User user) throws DuplicationException, NotFoundException, ValidationException;

    User getUserById(Long userId) throws NotFoundException;

    List<User> getAllUser();

    void deleteUser(Long userId);

    void validate(User user) throws ValidationException;
}
