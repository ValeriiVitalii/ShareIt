package ru.practicum.shareit.features.item.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.features.booking.model.BookingShortIdWithBookerId;
import ru.practicum.shareit.validations.PatchValidationGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    Long id;

    @NotBlank(groups = PatchValidationGroup.NameNotBlank.class)
    String name;

    @NotBlank(groups = PatchValidationGroup.DescriptionNotBlank.class)
    String description;

    @NotNull(groups = PatchValidationGroup.AvailableNotBlank.class)
    Boolean available;

    BookingShortIdWithBookerId lastBooking;

    BookingShortIdWithBookerId nextBooking;

    List<CommentDto> comments;
}
