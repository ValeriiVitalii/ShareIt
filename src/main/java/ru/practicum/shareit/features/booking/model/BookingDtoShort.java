package ru.practicum.shareit.features.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.features.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoShort {

    @NotNull
    LocalDateTime start;

    @NotNull
    LocalDateTime end;

    @NotNull
    Long itemId;

    User booker;
}
