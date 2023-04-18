package ru.practicum.shareit.features.user.inMemory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.features.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInMemoryRepository implements UserInMemory {

    Map<Long, User> users = new HashMap<>();

    private long userId = 1;

    //Добавление нового пользователя
    @Override
    public User addUser(User user) {
        if (user.getId() == null) {
            user.setId(userId++);
        }
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    //Получить пользователя по ID
    @Override
    public User getUserById(Long userId) {
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
}
