package ru.practicum.shareit.features.booking.service;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.StatusException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.booking.model.BookingDto;
import ru.practicum.shareit.features.booking.model.BookingDtoShort;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingDto postBooking(Long userId, BookingDtoShort bookingDto) throws NotFoundException, ValidationException;

    BookingDto patchBooking(Long userId, Long bookingId, @RequestParam Boolean approved) throws ValidationException,
            NotFoundException;

    BookingDto getBookingById(Long userId, Long bookingId) throws NotFoundException, ValidationException;

    List<BookingDto> getAllBookingByBooker(Long userId, String status, Integer from, Integer size) throws NotFoundException, StatusException, ValidationException;

    List<BookingDto> getAllBookingByOwner(Long userId, String status, Integer from, Integer size) throws NotFoundException, StatusException, ValidationException;

    void startAndEndValidation(LocalDateTime start, LocalDateTime end) throws ValidationException;

    void checkOwnerAndUserId(Long userId, Long itemId) throws NotFoundException;

}
