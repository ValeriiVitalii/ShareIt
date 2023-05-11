package ru.practicum.shareit.request.repository;

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
import ru.practicum.shareit.features.item.Repository.ItemRepository;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.request.model.ItemAnswer;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.request.repository.ItemAnswerRepository;
import ru.practicum.shareit.features.request.repository.ItemRequestRepository;
import ru.practicum.shareit.features.user.UserRepository;
import ru.practicum.shareit.features.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemAnswerRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    ItemAnswerRepository itemAnswerRepository;

    User user = User.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .build();

    User user2 = User.builder()
            .name("Mark Doe")
            .email("mark.doe@example.com")
            .build();

    User user3 = User.builder()
            .name("Sam May")
            .email("sam.doe@22")
            .build();

    Item item = Item.builder()
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .owner(user)
            .build();

    Item item2 = Item.builder()
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .owner(user)
            .build();

    ItemRequest itemRequest = ItemRequest.builder()
            .description("Ищу отвертку")
            .creator(user)
            .created(LocalDateTime.now())
            .build();

    ItemRequest itemRequest2 = ItemRequest.builder()
            .description("Одолжите дрель")
            .creator(user2)
            .created(LocalDateTime.now())
            .build();

    ItemRequest itemRequest3 = ItemRequest.builder()
            .description("Ишу андройд")
            .creator(user3)
            .created(LocalDateTime.now())
            .build();

    ItemAnswer itemAnswer = ItemAnswer.builder()
            .itemRequest(itemRequest)
            .item(item)
            .build();

    @BeforeEach
    public void setUp() {
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        itemRepository.save(item);
        itemRepository.save(item2);

        itemRequestRepository.save(itemRequest);
        itemRequestRepository.save(itemRequest2);
        itemRequestRepository.save(itemRequest3);

        itemAnswerRepository.save(itemAnswer);
    }

    @Test
    public void findByItemRequestIdTest() {
        assertThat(itemAnswerRepository.findByItemRequestId(1L).get(0), equalTo(item));

        assertThat(itemAnswerRepository.findByItemRequestId(99L).size(), equalTo(0));
    }

}
