package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto item) throws ValidationException;

    ItemDto editItem(Long userId, Long id, ItemDto item) throws NotFoundException;

    ItemDto getItem(Long userId, Long id);

    List<ItemDto> getAllItem(Long userId) throws NotFoundException;

    List<ItemDto> getItem(Long userId, String text);
}
