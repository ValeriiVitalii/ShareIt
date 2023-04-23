package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {

    public Long id;

    public String name;

    public String description;

    public Boolean available;
}
