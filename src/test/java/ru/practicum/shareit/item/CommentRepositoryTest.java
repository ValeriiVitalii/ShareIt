package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.features.item.Repository.CommentRepository;
import ru.practicum.shareit.features.item.Repository.ItemRepository;
import ru.practicum.shareit.features.item.model.Comment;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.user.UserRepository;
import ru.practicum.shareit.features.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CommentRepository commentRepository;

    User user = User.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .build();

    Item item = Item.builder()
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .owner(user)
            .build();

    Comment comment = Comment.builder()
            .text("Всем советую!")
            .item(item)
            .user(user)
            .build();

    @BeforeEach
    public void setUp() {
        userRepository.save(user);
        itemRepository.save(item);
        commentRepository.save(comment);
    }

    @Test
    public void findCommentsByItemIdTest() {
        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());
        assertThat(1, equalTo(comments.size()));

        comments = commentRepository.findCommentsByItemId(99L);
        assertThat(0, equalTo(comments.size()));
    }
}
