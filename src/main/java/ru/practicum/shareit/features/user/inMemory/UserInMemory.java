package ru.practicum.shareit.features.user.inMemory;

import ru.practicum.shareit.features.user.model.User;

import java.util.List;

public interface UserInMemory {

    User addUser(User user);

    User getUserById(Long userId);

    List<User> getAllUser();

    void deleteUser(Long userId);
}
