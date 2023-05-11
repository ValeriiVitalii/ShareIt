package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.user.UserRepository;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.service.UserServiceDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    User user = User.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .build();

    User user2 = User.builder()
            .id(2L)
            .name("Alex")
            .email("alex.alex@example.com")
            .build();

    UserRepository userRepository = mock(UserRepository.class);
    UserServiceDao userServiceDao = new UserServiceDao(userRepository);

    @Test
    void postUserTest() throws ValidationException {
        when(userRepository.save(user))
                .thenReturn(user);

        User savedUser = userServiceDao.postUser(user);

        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());

        verify(userRepository).save(user);
    }

    @Test
    void patchUserTest() throws NotFoundException {
        when(userRepository.save(user))
                .thenReturn(user);
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        User savedUpdateUser = userServiceDao.patchUser(user.getId(), user);

        assertEquals(user.getId(), savedUpdateUser.getId());
        assertEquals(user.getName(), savedUpdateUser.getName());
        assertEquals(user.getEmail(), savedUpdateUser.getEmail());

        verify(userRepository).save(user);
    }

    @Test
    void getUserByIdTest() throws NotFoundException {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        User getUser = userServiceDao.getUserById(user.getId());

        assertEquals(user.getId(), getUser.getId());
        assertEquals(user.getName(), getUser.getName());
        assertEquals(user.getEmail(), getUser.getEmail());
        assertThrows(NotFoundException.class, () -> userServiceDao.getUserById(2L));

        verify(userRepository).findById(user.getId());
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>(List.of(user, user2));

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> savedUsers = userServiceDao.getAllUser();

        assertEquals(users.size(), savedUsers.size());
        verify(userRepository).findAll();
    }

    @Test
    void deleteUserTest() {
        userServiceDao.deleteUser(user.getId());

        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void validateTest() {
        User user2 = User.builder()
                .id(2L)
                .name("Костя")
                .email("Kostya")
                .build();
        assertThrows(ValidationException.class, () -> userServiceDao.validate(user2));
    }
}
