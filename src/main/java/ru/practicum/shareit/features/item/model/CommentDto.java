package ru.practicum.shareit.features.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    Long id;

    @NotBlank
    String text;

    String authorName;

    LocalDateTime created;
}
