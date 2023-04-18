package ru.practicum.shareit.features.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.features.booking.BookingStatus;
import ru.practicum.shareit.features.item.model.ItemShortIdWithName;
import ru.practicum.shareit.features.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    Long id;

    LocalDateTime start;

    LocalDateTime end;

    ItemShortIdWithName item;

    User booker;

    BookingStatus status;
}
