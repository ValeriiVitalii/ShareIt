package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.exceptions.StatusException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.booking.model.BookingDto;
import ru.practicum.shareit.features.booking.model.BookingDtoShort;
import ru.practicum.shareit.features.booking.service.BookingServiceDao;
import ru.practicum.shareit.features.item.Repository.ItemRepository;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.user.UserRepository;
import ru.practicum.shareit.features.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BookingServiceDao bookingServiceDao;

    User userOwner = User.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .build();

    User userBooker = User.builder()
            .id(2L)
            .name("Rick Morti")
            .email("rick@yandex.ru")
            .build();

    Item item = Item.builder()
            .id(1L)
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .owner(userOwner)
            .build();

    BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
            .start(LocalDateTime.of(2023, 7, 30, 14, 22, 22))
            .end(LocalDateTime.of(2023, 7, 30, 22, 22, 22))
            .itemId(item.getId())
            .build();

    @BeforeEach
    void setUp() throws ValidationException, NotFoundException {
        userRepository.save(userOwner);
        userRepository.save(userBooker);
        itemRepository.save(item);
        bookingServiceDao.postBooking(userBooker.getId(), bookingDtoShort);
    }

    @Test
    void postBookingTestFail() {
        BookingDtoShort bookingDtoShortFailEnd = bookingDtoShort;
        bookingDtoShortFailEnd.setEnd(LocalDateTime.now());

        assertThrows(ValidationException.class,
                () -> bookingServiceDao.postBooking(userBooker.getId(), bookingDtoShortFailEnd));

        bookingDtoShortFailEnd.setEnd(bookingDtoShortFailEnd.getStart());
        assertThrows(ValidationException.class,
                () -> bookingServiceDao.postBooking(userBooker.getId(), bookingDtoShortFailEnd));

        assertThrows(NotFoundException.class, () -> bookingServiceDao.postBooking(userOwner.getId(), bookingDtoShort));
    }

    @Test
    void patchBookingTest() throws ValidationException, NotFoundException {
        assertThrows(NotFoundException.class,
                () -> bookingServiceDao.patchBooking(userBooker.getId(), 1L, true));

        BookingDto bookingDto = bookingServiceDao.patchBooking(userOwner.getId(), 1L, true);
        assertNotNull(bookingDto.getId());
        assertThat(bookingDto.getStart(), equalTo(bookingDtoShort.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(bookingDtoShort.getEnd()));
        assertThat(bookingDto.getItem().getId(), equalTo(bookingDtoShort.getItemId()));

        assertThrows(ValidationException.class,
                () -> bookingServiceDao.patchBooking(userOwner.getId(), 1L, true));
    }

    @Test
    void getBookingByIdTest() throws NotFoundException {
        assertThrows(NotFoundException.class,
                () -> bookingServiceDao.getBookingById(userBooker.getId(), 99L));
        assertThrows(NotFoundException.class,
                () -> bookingServiceDao.getBookingById(99L, 1L));

        BookingDto bookingDto = bookingServiceDao.getBookingById(userBooker.getId(), 1L);

        assertNotNull(bookingDto.getId());
        assertThat(bookingDto.getStart(), equalTo(bookingDtoShort.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(bookingDtoShort.getEnd()));
        assertThat(bookingDto.getItem().getId(), equalTo(bookingDtoShort.getItemId()));
    }

    @Test
    void getAllBookingByUserTest() throws ValidationException, NotFoundException, StatusException {
        List<BookingDto> bookings = bookingServiceDao.getAllBookingByBooker(userBooker.getId(),
                "ALL", 0, 100);
        assertThat(bookings.size(), equalTo(1));

        assertThrows(ValidationException.class, () -> bookingServiceDao.getAllBookingByBooker(userBooker.getId(),
                "ALL", -5, 100));

        bookings = bookingServiceDao.getAllBookingByBooker(userBooker.getId(),
                "CURRENT", 0, 100);
        assertThat(bookings.size(), equalTo(0));

        assertThrows(StatusException.class, () -> bookingServiceDao.getAllBookingByBooker(userBooker.getId(),
                "FAIL", 0, 100));

        bookings = bookingServiceDao.getAllBookingByBooker(userBooker.getId(),
                "APPROVED", 0, 100);
        assertThat(bookings.size(), equalTo(0));

        bookingServiceDao.patchBooking(userOwner.getId(), 1L, true);
        bookings = bookingServiceDao.getAllBookingByBooker(userBooker.getId(),
                "APPROVED", 0, 100);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    void getAllBookingByOwnerTest() throws ValidationException, NotFoundException, StatusException {
        List<BookingDto> bookings = bookingServiceDao.getAllBookingByOwner(userOwner.getId(),
                "ALL", 0, 100);
        assertThat(bookings.size(), equalTo(1));

        assertThrows(ValidationException.class, () -> bookingServiceDao.getAllBookingByOwner(userOwner.getId(),
                "ALL", -5, 100));

        bookings = bookingServiceDao.getAllBookingByOwner(userOwner.getId(),
                "CURRENT", 0, 100);
        assertThat(bookings.size(), equalTo(0));

        assertThrows(StatusException.class, () -> bookingServiceDao.getAllBookingByOwner(userOwner.getId(),
                "FAIL", 0, 100));

        bookings = bookingServiceDao.getAllBookingByOwner(userOwner.getId(),
                "APPROVED", 0, 100);
        assertThat(bookings.size(), equalTo(0));

        bookingServiceDao.patchBooking(userOwner.getId(), 1L, true);
        bookings = bookingServiceDao.getAllBookingByOwner(userOwner.getId(),
                "APPROVED", 0, 100);
        assertThat(bookings.size(), equalTo(1));
    }
}
