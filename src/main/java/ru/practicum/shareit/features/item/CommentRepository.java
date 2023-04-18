package ru.practicum.shareit.features.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.features.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c where c.item.id = ?1")
    List<Comment> findCommentsByItemId(Long itemId);
}
