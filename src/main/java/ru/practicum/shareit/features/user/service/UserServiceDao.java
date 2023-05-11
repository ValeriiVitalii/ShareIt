package ru.practicum.shareit.features.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.UserRepository;

import java.util.List;


@Service
@Primary
@Slf4j
@AllArgsConstructor
public class UserServiceDao implements UserService {


    private final UserRepository userRepository;

    @Override
    public User postUser(User user) throws ValidationException {
        validate(user);
        User userWithId = userRepository.save(user);
        log.info("Добавлен новый пользователь c ID={}", userWithId.getId());
        return userWithId;
    }

    @Override
    public User patchUser(Long userId, User updateUser) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с айди:" + userId + " не найден!"));
        if (updateUser.getId() != null) {
            user.setId(updateUser.getId());
        } else {
            updateUser.setId(userId);
        }
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        userRepository.save(user);
        log.info("Внесены изменения в данные пользователя ID={}", user.getId());
        return user;
    }

    @Override
    public User getUserById(Long userId) throws NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с айди:" + userId + " не найден!"));
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void validate(User user) throws ValidationException {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Неправильный формат email");
        }
    }
}
