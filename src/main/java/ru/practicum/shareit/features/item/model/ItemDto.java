package ru.practicum.shareit.features.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.features.booking.model.BookingShortIdWithBookerId;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    Long id;

    String name;

    String description;

    Boolean available;

    BookingShortIdWithBookerId lastBooking;

    BookingShortIdWithBookerId nextBooking;

    List<CommentDto> comments;

    Long requestId;
}
