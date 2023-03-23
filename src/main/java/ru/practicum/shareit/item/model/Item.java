package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {

    public Long id;

    public String name;

    public String description;

    public Boolean available;

    public Long userId;

    public Item(ItemDto itemDto, Long userId) {
        id = itemDto.getId();
        name = itemDto.getName();
        description = itemDto.getDescription();
        available = itemDto.getAvailable();
        this.userId = userId;
    }
}
