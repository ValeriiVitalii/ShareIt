package ru.practicum.shareit.features.item.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    Long id;

    @NotBlank
    String text;

    String authorName;

    LocalDateTime created;
}
