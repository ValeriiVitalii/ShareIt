package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemService itemService;


    @Override
    public ItemDto createItem(Long userId, ItemDto item) throws ValidationException {
        return itemService.createItem(userId, item);

    }

    @Override
    public ItemDto editItem(Long userId, Long id, ItemDto item) throws NotFoundException {
        return itemService.editItem(userId, id, item);

    }

    @Override
    public ItemDto getItem(Long userId, Long id) {
        return itemService.getItem(userId, id);
    }

    @Override
    public List<ItemDto> getAllItem(Long userId) throws NotFoundException {
        return itemService.getAllItem(userId);
    }

    @Override
    public List<ItemDto> getItem(Long userId, String text) {
        return itemService.getItem(userId, text);
    }
}
