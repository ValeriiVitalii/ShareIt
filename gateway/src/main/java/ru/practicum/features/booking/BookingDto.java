package ru.practicum.features.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.features.item.ItemShortIdWithName;
import ru.practicum.features.user.User;

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
