package ru.practicum.shareit.features.item.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.item.inMemory.ItemInMemory;
import ru.practicum.shareit.features.item.model.*;
import ru.practicum.shareit.features.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceInMemory implements ItemService {

    private final ItemInMemory itemInMemory;

    private final UserService userService;

    @Override
    public ItemDto postItem(Long userId, ItemDto item) throws ValidationException, NotFoundException {
        validate(item);
        containsUser(userId);
        Item itemWithId = itemInMemory.addItem(userId, toItem(item, userId));

        log.info("Добавлен новая вещь c ID={}", itemWithId.getId());
        return toItemDto(itemWithId);
    }

    //Создание Comment
    @Override
    public CommentDto postComment(Long userId, Long itemId, CommentDto commentDto) {
        return commentDto;
    }

    @Override
    public ItemDto editItem(Long userId, Long id, ItemDto updateItem) throws NotFoundException {
        userValidate(userId);

        Item item = itemInMemory.getItemById(id);

        if (updateItem.getName() != null) {
            item.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null) {
            item.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        }

        log.info("Внесены изменения в данные вещи с ID={}", item.getId());
        return toItemDto(itemInMemory.addItem(item.getId(), item));

    }

    //Получение ItemDto по itemId
    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        return toItemDto(itemInMemory.getItemById(itemId));
    }

    //Получение Item по itemId
    @Override
    public Item getItem(Long itemId) {
        return itemInMemory.getItemById(itemId);
    }

    @Override
    public ItemShortIdWithName getItemShortIdWithNameById(Long itemId) {
        return new ItemShortIdWithName();
    }

    //Получение всех Item по userId
    @Override
    public List<ItemDto> getAllItemByUser(Long userId) throws NotFoundException {
        userValidate(userId);
        List<ItemDto> userItems = new ArrayList<>();

        itemInMemory.getAllItem().stream()
                .filter(i -> i.getOwner().getId().equals(userId))
                .forEach(i -> userItems.add(toItemDto(itemInMemory.getItemById(i.getId()))));
        return userItems;
    }

    //Получение Item по тексту
    @Override
    public List<ItemDto> getItemByText(Long userId, String text) {
        List<ItemDto> foundItems = new ArrayList<>();
        itemInMemory.getAllItem().stream()
                .filter(i -> findWord(i.getDescription(), text) && i.getAvailable())
                .forEach(i -> foundItems.add(toItemDto(itemInMemory.getItemById(i.getId()))));
        return foundItems;
    }

    @Override
    public void userValidate(Long userId) throws NotFoundException {
        boolean checkUser = itemInMemory.getAllItem().stream()
                .anyMatch(i -> i.getOwner().getId().equals(userId));
        if (!checkUser) {
            throw new NotFoundException("У этого пользователя нет вещей!");
        }
    }

    private boolean findWord(String textString, String word) {
        if (word.length() < 2) {
            return false;
        }
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();

        return lowerCaseTextString.contains(lowerCaseWord);
    }

    private void containsUser(Long userId) throws NotFoundException {
        userService.getUserById(userId);
    }

    @Override
    public void validate(ItemDto item) throws ValidationException {
        if (item.getName() == null || item.getName().equals("")) {
            throw new ValidationException("Название не может быть пустым!");
        }
        if (item.getDescription() == null) {
            throw new ValidationException("Описание не может быть пустым!");
        }
        if (item.getAvailable() == null || !item.getAvailable()) {
            throw new ValidationException("Вещь должна быть доступной!");
        }
    }

    @Override
    public ItemShortIdWithName toItemShortIdWithName(Item item) {
        return ItemShortIdWithName.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    @Override
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    @Override
    public Item toItem(ItemDto itemDto, Long userId) throws NotFoundException {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userService.getUserById(userId))
                .build();
    }
}
