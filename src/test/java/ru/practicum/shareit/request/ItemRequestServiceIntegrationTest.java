package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.item.Repository.ItemRepository;
import ru.practicum.shareit.features.item.Service.ItemServiceDao;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.item.model.ItemDto;
import ru.practicum.shareit.features.request.model.ItemRequestDto;
import ru.practicum.shareit.features.request.model.ItemRequestWithItems;
import ru.practicum.shareit.features.request.service.ItemRequestServiceDao;
import ru.practicum.shareit.features.user.UserRepository;
import ru.practicum.shareit.features.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestServiceIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemServiceDao itemServiceDao;

    @Autowired
    ItemRequestServiceDao itemRequestServiceDao;

    User user = User.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .build();

    User userRequest = User.builder()
            .id(2L)
            .name("Rick Morti")
            .email("rick@yandex.ru")
            .build();

    Item item = Item.builder()
            .id(1L)
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .owner(user)
            .build();

    ItemDto itemAnswer = ItemDto.builder()
            .id(2L)
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .requestId(1L)
            .build();

    ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("Ищу отвертку")
            .build();

    @BeforeEach
    void setUp() throws ValidationException, NotFoundException {
        userRepository.save(user);
        userRepository.save(userRequest);
        itemRepository.save(item);
        itemRequestServiceDao.postItemRequest(userRequest.getId(), itemRequestDto);
        itemServiceDao.postItem(user.getId(), itemAnswer);
    }

    @Test
    void postItemRequestTestFail() {
        assertThrows(NotFoundException.class, () -> itemRequestServiceDao.postItemRequest(99L, itemRequestDto));

        itemRequestDto.setDescription(null);
        assertThrows(ValidationException.class, () -> itemRequestServiceDao.postItemRequest(userRequest.getId(), itemRequestDto));
    }

    @Test
    void getItemRequestWithItemsByCreatorTest() throws NotFoundException {
        List<ItemRequestWithItems> itemRequestWithItemsList = itemRequestServiceDao.getItemRequestWithItemsByCreator(
                userRequest.getId());
        assertThat(itemRequestWithItemsList.size(), equalTo(1));

        itemRequestWithItemsList = itemRequestServiceDao.getItemRequestWithItemsByCreator(
                user.getId());
        assertThat(itemRequestWithItemsList.size(), equalTo(0));
    }

    @Test
    void getItemRequestWithItemsUserIdTest() throws ValidationException, NotFoundException {
        List<ItemRequestWithItems> itemRequestWithItemsList = itemRequestServiceDao.getItemRequestWithItemsUserId(
                user.getId(), 0, 100);
        assertThat(itemRequestWithItemsList.size(), equalTo(1));

        itemRequestWithItemsList = itemRequestServiceDao.getItemRequestWithItemsUserId(
                userRequest.getId(), 0, 100);
        assertThat(itemRequestWithItemsList.size(), equalTo(0));

        assertThrows(ValidationException.class, () -> itemRequestServiceDao.getItemRequestWithItemsUserId(
                user.getId(), -1, 100));
    }

    @Test
    void getItemRequestWithItemsByIdTest() throws NotFoundException {
        ItemRequestWithItems itemRequestWithItems = itemRequestServiceDao.getItemRequestWithItemsById(
                userRequest.getId(), 1L);

        assertThat(itemRequestWithItems.getId(), equalTo(1L));
        assertThat(itemRequestWithItems.getItems().get(0), equalTo(itemAnswer));
    }
}
