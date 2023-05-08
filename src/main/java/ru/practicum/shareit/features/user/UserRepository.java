package ru.practicum.shareit.features.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.features.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
