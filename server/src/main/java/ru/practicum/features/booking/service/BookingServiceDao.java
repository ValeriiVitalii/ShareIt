package ru.practicum.features.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.features.booking.BookingRepository;
import ru.practicum.features.booking.BookingStatus;
import ru.practicum.features.booking.model.Booking;
import ru.practicum.features.booking.model.BookingDto;
import ru.practicum.features.booking.model.BookingDtoShort;
import ru.practicum.features.item.service.ItemService;
import ru.practicum.features.user.model.User;
import ru.practicum.features.user.service.UserService;
import ru.practicum.exceptions.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceDao implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public BookingDto postBooking(Long userId, BookingDtoShort bookingDto) throws NotFoundException, ValidationException {
        if (!itemService.getItemById(bookingDto.getItemId(), userId).getAvailable()) {
            throw new ValidationException("Вещь уже находится в аренде!");
        }
        checkOwnerAndUserId(userId, bookingDto.getItemId());

        Booking booking = setStatusToWaitingAndBooker(toBooking(bookingDto), userId);

        startAndEndValidation(booking.getStart(), booking.getEnd());
        Booking bookingWithId = bookingRepository.save(booking);
        log.info("Добавлена новая аренда c ID={}", bookingWithId.getId());
        return toBookingDto(bookingWithId);
    }

    //Изменение статуса Booking
    @Override
    public BookingDto patchBooking(Long userId, Long bookingId, Boolean approved) throws ValidationException, NotFoundException {
        BookingDto bookingDto = getBookingById(userId, bookingId);

        checkOwnerItem(userId, bookingDto.getItem().getId());
        setStatusToApprovedOrRejected(approved, bookingDto);

        log.info("Внесены изменения в бронировании с ID={}", bookingDto.getId());
        bookingRepository.setStatusById(bookingId, bookingDto.getStatus());
        return bookingDto;
    }

    //Получение Booking по id
    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) throws NotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с айди:" + bookingId + " не найдено!"));
        if (!(booking.getBooker().getId().equals(userId) || booking.getItemId().getOwner().getId().equals(userId))) {
            throw new NotFoundException("Вы не можете посмотреть эту аренду!");
        }
        return toBookingDto(booking);
    }

    //Получение всех Booking по booker_id
    @Override
    public List<BookingDto> getAllBookingByBooker(Long userId, String status, Integer from, Integer size) throws NotFoundException, StatusException, ValidationException {
        User user = userService.getUserById(userId);
        PageRequest pageable = getPageRequest(from, size);
        List<BookingDto> bookings = toBookingsDto(bookingRepository.findAllByBookerOrderByStartDesc(user, pageable));

        if (status.equals("ALL") || status.equals("FUTURE")) {
            return bookings;
        }
        if (status.equals("CURRENT")) {
            return bookings.stream()
                    .filter(bookingDto -> bookingDto.getEnd().isAfter(LocalDateTime.now()) && bookingDto.getStart().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(BookingDto::getStart))
                    .collect(Collectors.toList());
        }
        if (status.equals("PAST")) {
            return bookings.stream()
                    .filter(bookingDto -> bookingDto.getEnd().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        BookingStatus bookingStatus = checkStatus(status);
        return toBookingsDto(bookingRepository.findAllByBookerAndStatusOrderByStartDesc(user, bookingStatus, pageable));
    }

    //Получение всех Booking по id Владельца Item
    @Override
    public List<BookingDto> getAllBookingByOwner(Long userId, String status, Integer from, Integer size) throws NotFoundException, StatusException, ValidationException {
        User user = userService.getUserById(userId);
        PageRequest pageable = getPageRequest(from, size);
        List<BookingDto> bookings = toBookingsDto(bookingRepository.findAllByItemIdOwnerOrderByStartDesc(user, pageable));

        if (status.equals("ALL") || status.equals("FUTURE")) {
            return bookings;
        }
        if (status.equals("CURRENT")) {
            return bookings.stream()
                    .filter(bookingDto -> bookingDto.getEnd().isAfter(LocalDateTime.now()) && bookingDto.getStart().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(BookingDto::getStart))
                    .collect(Collectors.toList());
        }
        if (status.equals("PAST")) {
            return bookings.stream()
                    .filter(bookingDto -> bookingDto.getEnd().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        BookingStatus bookingStatus = checkStatus(status);
        return toBookingsDto(bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(user, bookingStatus, pageable));
    }

    @Override
    public void startAndEndValidation(LocalDateTime start, LocalDateTime end) throws ValidationException {
        LocalDateTime presentTime = LocalDateTime.now();
        if (start.isBefore(presentTime) || end.isBefore(presentTime)) {
            throw new ValidationException("Время начала или конца аренды не может быть раньше нынешнего времени!");
        }
        if (start.equals(end) || start.isAfter(end)) {
            throw new ValidationException("Укажите корректную дату окончания аренды");
        }
    }

    private void checkOwnerItem(Long userId, Long itemId) throws NotFoundException {
        if (!itemService.getItem(itemId).getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вы не являетесь владельцем вещи!");
        }
    }

    @Override
    public void checkOwnerAndUserId(Long userId, Long itemId) throws NotFoundException {
        Long ownerId = itemService.getItem(itemId).getOwner().getId();
        if (ownerId.equals(userId)) {
            throw new NotFoundException("Вы не можете взять вещь с id=" + itemId +
                    " в аренду поскольку, вы являетесь владельцем данной вещи") ;
        }
    }

    private Booking setStatusToWaitingAndBooker(Booking booking, Long userId) throws NotFoundException {
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(userService.getUserById(userId));
        return booking;
    }

    private void setStatusToApprovedOrRejected(Boolean approved, BookingDto bookingDto) throws ValidationException {
        if (approved) {
            if (bookingDto.getStatus().equals(BookingStatus.APPROVED)) {
                throw new ValidationException("Аренда уже одобрена.");
            }

            bookingDto.setStatus(BookingStatus.APPROVED);
        } else {
            bookingDto.setStatus(BookingStatus.REJECTED);
        }
    }

    private BookingStatus checkStatus(String status) throws StatusException {
        try {
            return BookingStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new StatusException("Invalid booking status");
        }
    }

    private PageRequest getPageRequest(Integer from, Integer size) throws ValidationException {
        if (from < 0) {
            throw new ValidationException(String.format("Некорректное значение from = %d", from));
        }
        return PageRequest.of(from / size, size);
    }

    public BookingDto toBookingDto(Booking booking) throws NotFoundException {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemService.getItemShortIdWithNameById(booking.getItemId().getId()))
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public List<BookingDto> toBookingsDto(List<Booking> bookings) {
        return bookings.stream()
                .map(booking -> {
                    try {
                        return toBookingDto(booking);
                    } catch (NotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Booking toBooking(BookingDtoShort bookingDtoShort) throws NotFoundException {
        return Booking.builder()
                .start(bookingDtoShort.getStart())
                .end(bookingDtoShort.getEnd())
                .itemId(itemService.getItem(bookingDtoShort.getItemId()))
                .booker(bookingDtoShort.getBooker())
                .build();
    }
}
