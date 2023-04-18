package ru.practicum.shareit.features.item.Service;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.item.model.*;

import java.util.List;

public interface ItemService {

    ItemDto postItem(Long userId, ItemDto item) throws ValidationException, NotFoundException;

    CommentDto postComment(Long userId, Long itemId, CommentDto comment) throws NotFoundException, ValidationException;

    ItemDto editItem(Long userId, Long id, ItemDto item) throws NotFoundException;

    ItemDto getItemById(Long itemId, Long userId) throws NotFoundException;

    Item getItem(Long itemId) throws NotFoundException;

    ItemShortIdWithName getItemShortIdWithNameById(Long itemId) throws NotFoundException;

    List<ItemDto> getAllItemByUser(Long userId) throws NotFoundException;

    List<ItemDto> getItemByText(Long userId, String text);

    void userValidate(Long userId) throws NotFoundException;


    void validate(ItemDto item) throws ValidationException;

    ItemShortIdWithName toItemShortIdWithName(Item item);

    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto, Long userId) throws NotFoundException;
}
