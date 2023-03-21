package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {

    long bookingId;

    Item item;

    Date toDate;

    Date fromDate;
}
