package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.features.booking.BookingRepository;
import ru.practicum.shareit.features.booking.BookingStatus;
import ru.practicum.shareit.features.booking.model.Booking;
import ru.practicum.shareit.features.item.Repository.ItemRepository;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.user.UserRepository;
import ru.practicum.shareit.features.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BookingRepository bookingRepository;

    User userBookerBooking1And2 = User.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .build();

    User userBookerBooking3 = User.builder()
            .name("Mark Doe")
            .email("mark.doe@example.com")
            .build();

    Item item = Item.builder()
            .name("Дрель")
            .description("Рабочая Дрель")
            .available(true)
            .owner(userBookerBooking3)
            .build();

    Booking booking = Booking.builder()
            .start(LocalDateTime.of(2023, 7, 30, 14, 22, 22))
            .end(LocalDateTime.of(2023, 7, 30, 22, 22, 22))
            .itemId(item)
            .booker(userBookerBooking1And2)
            .status(BookingStatus.WAITING)
            .build();

    Booking booking2 = Booking.builder()
            .start(LocalDateTime.of(2023, 9, 11, 14, 4, 3))
            .end(LocalDateTime.of(2023, 9, 23, 22, 6, 3))
            .itemId(item)
            .booker(userBookerBooking1And2)
            .status(BookingStatus.WAITING)
            .build();

    Booking booking3 = Booking.builder()
            .start(LocalDateTime.of(2023, 10, 29, 6, 24, 1))
            .end(LocalDateTime.of(2023, 11, 2, 6, 46, 1))
            .itemId(item)
            .booker(userBookerBooking3)
            .status(BookingStatus.WAITING)
            .build();

    PageRequest pageable = PageRequest.of(0 / 100, 100);

    @BeforeEach
    public void setUp() {
        userRepository.save(userBookerBooking1And2);
        userRepository.save(userBookerBooking3);

        itemRepository.save(item);

        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
    }

    @Test
    public void setStatusByIdTest() {
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.setStatusById(1L, BookingStatus.APPROVED);

        assertThat(booking, equalTo(bookingRepository.findById(1L).get()));
        assertThat(booking2, equalTo(bookingRepository.findById(2L).get()));
    }

    @Test
    public void findAllByBookerOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDesc(userBookerBooking1And2, pageable);
        assertThat(bookings.size(), equalTo(2));
        assertThat(bookings.get(0), equalTo(booking2));
        assertThat(bookings.get(1), equalTo(booking));

        bookings = bookingRepository.findAllByBookerOrderByStartDesc(userBookerBooking3, pageable);
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0), equalTo(booking3));
    }

    @Test
    public void findAllByBookerAndStatusOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(
                userBookerBooking1And2, BookingStatus.WAITING, pageable);

        assertThat(bookings.size(), equalTo(2));
        assertThat(bookings.get(0), equalTo(booking2));
        assertThat(bookings.get(1), equalTo(booking));

        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.setStatusById(booking.getId(), BookingStatus.APPROVED);
        bookings = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(
                userBookerBooking1And2, BookingStatus.APPROVED, pageable);

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0), equalTo(booking));
    }

    @Test
    public void findAllByItemIdOwnerOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.findAllByItemIdOwnerOrderByStartDesc(
                userBookerBooking3, pageable);

        assertThat(bookings.size(), equalTo(3));
        assertThat(bookings.get(0), equalTo(booking3));
        assertThat(bookings.get(1), equalTo(booking2));
        assertThat(bookings.get(2), equalTo(booking));

        bookings = bookingRepository.findAllByItemIdOwnerOrderByStartDesc(
                userBookerBooking1And2, pageable);
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    public void findAllByItemIdOwnerAndStatusOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(
                userBookerBooking3, BookingStatus.WAITING, pageable);

        assertThat(bookings.size(), equalTo(3));
        assertThat(bookings.get(0), equalTo(booking3));
        assertThat(bookings.get(1), equalTo(booking2));
        assertThat(bookings.get(2), equalTo(booking));

        bookings = bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(
                userBookerBooking1And2, BookingStatus.WAITING, pageable);
        assertThat(bookings.size(), equalTo(0));

        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.setStatusById(booking.getId(), BookingStatus.APPROVED);
        bookings = bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(
                userBookerBooking3, BookingStatus.APPROVED, pageable);

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0), equalTo(booking));
    }

    @Test
    public void findAllByItemIdOrderByStartAscTest() {
        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStartAsc(item.getId());

        assertThat(bookings.size(), equalTo(3));
        assertThat(bookings.get(0), equalTo(booking));
        assertThat(bookings.get(1), equalTo(booking2));
        assertThat(bookings.get(2), equalTo(booking3));

        bookings = bookingRepository.findAllByItemIdOrderByStartAsc(99L);
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    public void findByBookerAndItemTest() {
        List<Booking> bookings = bookingRepository.findByBookerAndItem(
                userBookerBooking1And2.getId(), item.getId(), BookingStatus.WAITING);

        assertThat(bookings.size(), equalTo(2));
        assertThat(bookings.get(0), equalTo(booking));
        assertThat(bookings.get(1), equalTo(booking2));

        bookings = bookingRepository.findByBookerAndItem(
                userBookerBooking3.getId(), item.getId(), BookingStatus.WAITING);

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0), equalTo(booking3));

        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.setStatusById(booking.getId(), BookingStatus.APPROVED);
        bookings = bookingRepository.findByBookerAndItem(
                userBookerBooking1And2.getId(), item.getId(), BookingStatus.APPROVED);

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0), equalTo(booking));
    }
}
