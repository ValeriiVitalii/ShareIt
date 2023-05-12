package ru.practicum.features.user;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.features.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
