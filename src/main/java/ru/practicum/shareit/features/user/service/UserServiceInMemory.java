package ru.practicum.shareit.features.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.user.inMemory.UserInMemory;
import ru.practicum.shareit.features.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceInMemory implements UserService {

    UserInMemory userInMemory;

    @Override
    public User postUser(User user) throws ValidationException, DuplicationException {
        validate(user);
        User userWithId = userInMemory.addUser(user);
        log.info("Добавлен новый пользователь ID={}", userWithId.getId());
        return userInMemory.getUserById(user.getId());
    }

    @Override
    public User patchUser(Long userId, User updateUser) throws DuplicationException {
        User user = userInMemory.getUserById(userId);

        if (updateUser.getId() != null) {
            user.setId(updateUser.getId());
        } else {
            updateUser.setId(userId);
        }
        if (updateUser.getEmail() != null) {
            checkDuplicationEmail(updateUser);
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }

        log.info("Внесены изменения в данные пользователя ID={}", user.getId());
        return userInMemory.addUser(user);
    }

    @Override
    public User getUserById(Long userId) throws NotFoundException {
        if (userInMemory.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с айди:" + userId + " не найден!");
        }
        return userInMemory.getUserById(userId);
    }

    @Override
    public List<User> getAllUser() {
        return userInMemory.getAllUser();
    }

    @Override
    public void deleteUser(Long userId) {
        userInMemory.deleteUser(userId);
    }

    private void checkDuplicationEmail(User user) throws DuplicationException {
        if (userInMemory.getAllUser().stream()
                .anyMatch(u -> u.getEmail().contains(user.getEmail()) && !u.getId().equals(user.getId()))) {
            throw new DuplicationException("Email уже зарегестрирован");
        }
    }

    private void validate(User user) throws ValidationException, DuplicationException {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Неправильный формат email");
        }
        checkDuplicationEmail(user);
    }
}
