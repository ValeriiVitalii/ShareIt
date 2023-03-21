package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.DuplicationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserStorage implements UserService {

    Map<Long, User> users = new HashMap<>();

    private long userId = 1;

    //Добавление нового пользователя
    @Override
    public User createUser(User user) throws ValidationException, DuplicationException {
        User userValidate = validate(user);

        users.put(userValidate.getId(), userValidate);
        log.info("Добавлен новый пользователь с ID=" + userValidate.getId());
        return users.get(user.getId());
    }

    //Апгрейд данных пользователя
    @Override
    public User editUser(Long userId, User updateUser) throws DuplicationException {
        User user = users.get(userId);

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

        log.info("Внесены изменения в данные пользователя с ID=" + user.getId());
        users.put(userId, user);
        return users.get(userId);
    }

    //Получить пользователя по ID
    @Override
    public User getUser(Long userId) throws NotFoundException {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с айди:" + userId + " не найден!");
        }
        return users.get(userId);
    }

    //Получение всех пользователей
    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(users.values());
    }

    //Удаление пользователя
    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    private void checkDuplicationEmail(User user) throws DuplicationException {
        if (users.values().stream()
                .anyMatch(u -> u.getEmail().contains(user.getEmail()) && !u.getId().equals(user.getId()))) {
            throw new DuplicationException("Email уже зарегестрирован");
        }
    }

    private User validate(User user) throws ValidationException, DuplicationException {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Неправильный формат email");
        }
        checkDuplicationEmail(user);

        if (user.getId() == null) {
            user.setId(userId++);
        }
        return user;
    }
}
