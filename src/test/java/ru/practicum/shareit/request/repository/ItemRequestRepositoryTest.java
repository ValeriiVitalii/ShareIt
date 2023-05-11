
package ru.practicum.shareit.request.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.features.item.Repository.ItemRepository;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.request.repository.ItemRequestRepository;
import ru.practicum.shareit.features.user.UserRepository;
import ru.practicum.shareit.features.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

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

    User userWithoutRequest = User.builder()
            .name("Alex Le")
            .email("Alllexxx.lee@gmail.com")
            .build();

    Item item = Item.builder()
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


    @BeforeEach
    public void setUp() {
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(userWithoutRequest);

        itemRepository.save(item);

        itemRequestRepository.save(itemRequest);
        itemRequestRepository.save(itemRequest2);
        itemRequestRepository.save(itemRequest3);
    }

    @Test
    public void findAllByCreatorTest() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByCreator(user);
        assertThat(itemRequests.get(0), equalTo(itemRequest));

        itemRequests = itemRequestRepository.findAllByCreator(user2);
        assertThat(itemRequests.get(0), equalTo(itemRequest2));

        itemRequests = itemRequestRepository.findAllByCreator(user3);
        assertThat(itemRequests.get(0), equalTo(itemRequest3));

        itemRequests = itemRequestRepository.findAllByCreator(userWithoutRequest);
        assertThat(itemRequests.size(), equalTo(0));
    }

    @Test
    public void findAllItemRequestExceptCreatorTest() {
        PageRequest pageable = PageRequest.of(0 / 100, 100);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllItemRequestExceptCreator(user.getId(), pageable);

        assertThat(itemRequests.size(), equalTo(2));
    }

    @Test
    public void findItemRequestByIdTest() {
        assertThat(itemRequestRepository.findItemRequestById(1L), equalTo(itemRequest));
        assertThat(itemRequestRepository.findItemRequestById(2L), equalTo(itemRequest2));
        assertThat(itemRequestRepository.findItemRequestById(3L), equalTo(itemRequest3));

        assertNull(itemRequestRepository.findItemRequestById(99L));
    }


}
