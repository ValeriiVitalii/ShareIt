package ru.practicum.features.item.service;


import ru.practicum.features.item.model.CommentDto;
import ru.practicum.features.item.model.Item;
import ru.practicum.features.item.model.ItemDto;
import ru.practicum.exceptions.*;
import ru.practicum.features.item.model.ItemShortIdWithName;

import java.util.List;

public interface ItemService {

    ItemDto postItem(Long userId, ItemDto item) throws ValidationException, NotFoundException;

    CommentDto postComment(Long userId, Long itemId, CommentDto comment) throws NotFoundException, ValidationException;

    ItemDto editItem(Long userId, Long id, ItemDto item) throws NotFoundException;

    ItemDto getItemById(Long itemId, Long userId) throws NotFoundException;

    Item getItem(Long itemId) throws NotFoundException;

    ItemShortIdWithName getItemShortIdWithNameById(Long itemId) throws NotFoundException;

    List<ItemDto> getAllItemByUser(Long userId, Integer from, Integer size) throws NotFoundException, ValidationException;

    List<ItemDto> getItemByText(Long userId, String text, Integer from, Integer size) throws ValidationException;

    void userValidate(Long userId) throws NotFoundException;


    void validate(ItemDto item) throws ValidationException;

    ItemShortIdWithName toItemShortIdWithName(Item item);

    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto, Long userId) throws NotFoundException;
}
