package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class InMemoryItemStorage implements ItemService {

    private long itemId = 1;

    Map<Long, Item> items = new HashMap<>();


    //Доабвление новой вещи
    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) throws ValidationException {
        ItemDto itemWithId = validate(itemDto);

        items.put(itemWithId.getId(), new Item(itemDto, userId));
        log.info("Добавлен новая вещь с id=" + itemWithId.getId());
        return itemWithId;
    }

    //Апгрейд данных вещи
    @Override
    public ItemDto editItem(Long userId, Long id, ItemDto updateItem) throws NotFoundException {
        userValidate(userId);

        Item item = items.get(id);

        if(updateItem.getId() != null) {
            item.setId(updateItem.getId());
        } else {
            updateItem.setId(userId);
        }
        if(updateItem.getName() != null) {
            item.setName(updateItem.getName());
        }
        if(updateItem.getDescription() != null) {
            item.setDescription(updateItem.getDescription());
        }
        if(updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        }

        items.put(item.getId(), item);
        log.info("Внесены изменения в данные вещи с ID=" + item.getId());
        return mapRowToItemDto(items.get(id));
    }

    //Получить вещь по ID
    @Override
    public ItemDto getItem(Long userId, Long id) {
        return mapRowToItemDto(items.get(id));
    }

    //Получить все вещи по ID пользователя
    @Override
    public List<ItemDto> getAllItem(Long userId) throws NotFoundException {
        userValidate(userId);
        List<ItemDto> userItems = new ArrayList<>();

        items.values().stream()
                .filter(i -> i.getUserId().equals(userId))
                .forEach(i -> userItems.add(mapRowToItemDto(
                        items.get(i.getId()))));
        return userItems;
    }

    //Получить вещи по тексту
    @Override
    public List<ItemDto> getItem(Long userId, String text) {
        List<ItemDto> foundItems = new ArrayList<>();
        items.values().stream()
                 .filter(i -> findWord(i.getDescription(), text) && i.getAvailable())
                 .forEach(i -> foundItems.add(mapRowToItemDto(
                        items.get(i.getId()))));
        return foundItems;
    }

    private boolean findWord(String textString, String word) {
        if(word.length() < 2) {
            return false;
        }
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();

        return lowerCaseTextString.contains(lowerCaseWord);
    }

    private void userValidate(Long userId) throws NotFoundException {
        boolean checkUser = items.values().stream()
                .anyMatch(i -> i.getUserId().equals(userId));
        if(!checkUser) {
            throw new NotFoundException("У этого пользователя нет вещей!");
        }
    }

    private ItemDto mapRowToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    private ItemDto validate(ItemDto item) throws ValidationException {
        if(item.getName() == null || item.getName().equals("")) {
            throw new ValidationException("Название не может быть пустым!");
        }
        if(item.getDescription() == null) {
            throw new ValidationException("Описание не может быть пустым!");
        }
        if(item.getAvailable() == null || !item.getAvailable()) {
            throw new ValidationException("Вещь должна быть доступной!");
        }
        item.setId(itemId++);
        return item;
    }
}
