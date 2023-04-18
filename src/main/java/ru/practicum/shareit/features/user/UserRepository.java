package ru.practicum.shareit.features.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.features.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.name = ?1 WHERE u.id = ?2")
    void setName(String name, Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.email = ?1 WHERE u.id = ?2")
    void setEmail(String email, Long id);
}
