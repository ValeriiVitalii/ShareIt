package ru.practicum.shareit.item.Service;

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
import ru.practicum.shareit.features.booking.BookingRepository;
import ru.practicum.shareit.features.booking.BookingStatus;
import ru.practicum.shareit.features.booking.model.Booking;
import ru.practicum.shareit.features.item.Service.ItemServiceDao;
import ru.practicum.shareit.features.item.model.CommentDto;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.item.model.ItemDto;
import ru.practicum.shareit.features.item.model.ItemShortIdWithName;
import ru.practicum.shareit.features.user.UserRepository;
import ru.practicum.shareit.features.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceIntegrationTest {

    @Autowired
    ItemServiceDao itemServiceDao;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    User user = User.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .build();

    User userBooker = User.builder()
            .name("Mark Doe")
            .email("mark.doe@example.com")
            .build();

    ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .build();

    Item item = Item.builder()
            .id(1L)
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .owner(user)
            .build();

    CommentDto commentDto = CommentDto.builder()
            .text("Всем советую!")
            .build();

    ItemShortIdWithName itemShortIdWithName = ItemShortIdWithName.builder()
            .id(1L)
            .name("Дрель")
            .build();

    Booking booking = Booking.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.of(2023, 7, 30, 22, 22, 22))
            .itemId(item)
            .booker(userBooker)
            .status(BookingStatus.APPROVED)
            .build();

    @BeforeEach
    void setUp() throws ValidationException, NotFoundException {
        userRepository.save(user);
        userRepository.save(userBooker);

        itemServiceDao.postItem(user.getId(), itemDto);
        bookingRepository.save(booking);
        itemServiceDao.postComment(userBooker.getId(), itemDto.getId(), commentDto);
    }

    @Test
    void postItemTestFail() {
        assertThrows(ValidationException.class, () -> itemServiceDao.postItem(user.getId(), new ItemDto()));
        assertThrows(NotFoundException.class, () -> itemServiceDao.postItem(99L, itemDto));
    }

    @Test
    void postCommentsFailTest() {
        assertThrows(ValidationException.class, () -> itemServiceDao.postComment(user.getId(), 99L, commentDto));
    }

    @Test
    void editItemTest() throws NotFoundException {
        ItemDto itemDtoFail = itemDto;
        itemDtoFail.setId(99L);
        assertThrows(NotFoundException.class, () -> itemServiceDao.editItem(user.getId(), itemDtoFail.getId(), itemDtoFail));

        itemDtoFail.setId(1L);
        itemDtoFail.setName("Ножницы");

        ItemDto itemDtoNew = itemServiceDao.editItem(user.getId(), itemDtoFail.getId(), itemDtoFail);

        assertThat(itemDto.getId(), equalTo(itemDtoNew.getId()));
        assertThat(itemDto.getName(), equalTo(itemDtoNew.getName()));
    }

    @Test
    void getItemDtoByIdTest() throws NotFoundException {
        ItemDto itemDtoGet = itemServiceDao.getItemById(itemDto.getId(), user.getId());

        assertThat(itemDto.getId(), equalTo(itemDtoGet.getId()));
        assertThat(itemDto.getName(), equalTo(itemDtoGet.getName()));
        assertThat(itemDto.getDescription(), equalTo(itemDtoGet.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(itemDtoGet.getAvailable()));
    }

    @Test
    void getItemByIdTest() throws NotFoundException {
        Item itemGet = itemServiceDao.getItem(itemDto.getId());

        assertThat(item.getId(), equalTo(itemGet.getId()));
        assertThat(item.getName(), equalTo(itemGet.getName()));
        assertThat(item.getDescription(), equalTo(itemGet.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemGet.getAvailable()));
    }

    @Test
    void getItemShortIdWithNameByIdTest() throws NotFoundException {
        ItemShortIdWithName itemServiceDaoItemGet = itemServiceDao.getItemShortIdWithNameById(itemDto.getId());

        assertThat(itemShortIdWithName.getId(), equalTo(itemServiceDaoItemGet.getId()));
        assertThat(itemShortIdWithName.getName(), equalTo(itemServiceDaoItemGet.getName()));
    }

    @Test
    void getAllItemByUserTest() throws NotFoundException, ValidationException {
        List<ItemDto> items = itemServiceDao.getAllItemByUser(user.getId(), 0, 100);

        assertThrows(ValidationException.class, () -> itemServiceDao.getAllItemByUser(user.getId(), -4, 100));
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void getItemByTextTest() throws ValidationException {
        List<ItemDto> items = itemServiceDao.getItemByText(user.getId(), "Дрель", 0, 100);

        assertThat(items.size(), equalTo(1));
    }

}