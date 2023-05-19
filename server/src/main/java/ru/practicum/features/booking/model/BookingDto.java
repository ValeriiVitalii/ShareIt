package ru.practicum.features.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.features.booking.BookingStatus;
import ru.practicum.features.item.model.ItemShortIdWithName;
import ru.practicum.features.user.model.User;
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
