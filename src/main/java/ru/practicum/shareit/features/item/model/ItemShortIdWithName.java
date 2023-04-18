package ru.practicum.shareit.features.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemShortIdWithName {

    Long id;

    String name;
}
