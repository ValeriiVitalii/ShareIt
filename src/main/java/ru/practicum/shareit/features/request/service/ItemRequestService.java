package ru.practicum.shareit.features.request.service;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.request.model.ItemRequestDto;
import ru.practicum.shareit.features.request.model.ItemRequestWithItems;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto postItemRequest(Long userId, ItemRequestDto itemRequestDto) throws NotFoundException, ValidationException;

    List<ItemRequestWithItems> getItemRequestWithItemsByCreator(Long userId) throws NotFoundException;

    List<ItemRequestWithItems> getItemRequestWithItemsUserId(Long userId, Integer from, Integer size) throws NotFoundException, ValidationException;

    ItemRequestWithItems getItemRequestWithItemsById(Long userId, Long requestId) throws NotFoundException;

}
