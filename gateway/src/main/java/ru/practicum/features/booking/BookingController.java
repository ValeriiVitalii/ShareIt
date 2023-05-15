
package ru.practicum.features.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.features.booking.model.BookingDtoShort;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private BookingClient bookingClient;

    static final String USER_ID = "X-Sharer-User-Id";
    static final String ID = "bookingId";


    @PostMapping
    public ResponseEntity<Object> postBooking(@RequestHeader(USER_ID) Long userId, @Valid @RequestBody BookingDtoShort bookingDto) {
        return bookingClient.postBooking(userId, bookingDto);
    }

    //Изменение статуса Booking
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchBooking(@RequestHeader(USER_ID) Long userId, @PathVariable(ID) Long bookingId,
                                               @RequestParam Boolean approved) {
        return bookingClient.patchBooking(userId, bookingId, approved);
    }

    //Получение Booking по id
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID) Long userId, @PathVariable(ID) Long bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    //Получение всех Booking по booker_id
    @GetMapping
    public ResponseEntity<Object> getAllBookingByBooker(@RequestHeader(USER_ID) Long userId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "100") Integer size) {
        return bookingClient.getAllBookingByBooker(userId, state, from, size);
    }

    //Получение всех Booking по id Владельца Item
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingByOwner(@RequestHeader(USER_ID) Long userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "100") Integer size) {
        return bookingClient.getAllBookingByOwner(userId, state, from, size);
    }


}
