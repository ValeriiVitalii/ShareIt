
package ru.practicum.features.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.StatusException;
import ru.practicum.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

   /* private BookingService bookingService;

    static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto postBooking(@RequestHeader(USER_ID) Long userId, @Valid @RequestBody BookingDtoShort bookingDto)
            throws ValidationException, NotFoundException {
        return bookingService.postBooking(userId, bookingDto);
    }

    //Изменение статуса Booking
    @PatchMapping("/{bookingId}")
    public BookingDto patchBooking(@RequestHeader(USER_ID) Long userId, @PathVariable("bookingId") Long bookingId,
                                   @RequestParam Boolean approved) throws ValidationException, NotFoundException {
        return bookingService.patchBooking(userId, bookingId, approved);
    }

    //Получение Booking по id
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(USER_ID) Long userId, @PathVariable("bookingId") Long bookingId)
            throws ValidationException, NotFoundException {
        return bookingService.getBookingById(userId, bookingId);
    }

    //Получение всех Booking по booker_id
    @GetMapping
    public List<BookingDto> getAllBookingByBooker(@RequestHeader(USER_ID) Long userId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size)
            throws NotFoundException, StatusException, ValidationException {
        return bookingService.getAllBookingByBooker(userId, state, from, size);
    }

    //Получение всех Booking по id Владельца Item
    @GetMapping("/owner")
    public List<BookingDto> getAllBookingByOwner(@RequestHeader(USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "100") Integer size)
            throws NotFoundException, StatusException, ValidationException {
        return bookingService.getAllBookingByOwner(userId, state, from, size);
    }*/


}
