package ru.practicum.shareit.features.booking.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingShortIdWithBookerId {

    Long id;

    Long BookerId;
}
