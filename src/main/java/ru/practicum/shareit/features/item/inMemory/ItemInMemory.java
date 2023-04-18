package ru.practicum.shareit.features.item.inMemory;

import ru.practicum.shareit.features.item.model.Item;

import java.util.List;

public interface ItemInMemory {

    Item addItem(Long userId, Item item);

    Item getItemById(Long itemId);

    List<Item> getAllItem();
}
