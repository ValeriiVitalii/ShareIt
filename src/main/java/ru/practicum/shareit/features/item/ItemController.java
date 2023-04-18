package ru.practicum.shareit.features.item;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.features.item.Service.ItemService;
import ru.practicum.shareit.features.item.model.CommentDto;
import ru.practicum.shareit.validations.PatchValidationGroup;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.item.model.ItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    static final String USER_ID = "X-Sharer-User-Id";

    //Создание Item
    @PostMapping
    public ItemDto postItem(@RequestHeader(USER_ID) Long userId,
                            @Valid @RequestBody ItemDto itemDto) throws NotFoundException, ValidationException {
        return itemService.postItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(USER_ID) Long userId,
                                  @PathVariable("itemId") Long itemId,
                                  @RequestBody CommentDto commentDto) throws NotFoundException, ValidationException {
        return itemService.postComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestHeader(USER_ID) Long userId, @PathVariable("itemId") Long itemId,
                             @Validated(PatchValidationGroup.class) @RequestBody ItemDto itemDto) throws NotFoundException {
        return itemService.editItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(USER_ID) Long userId, @PathVariable("itemId") Long itemId) throws NotFoundException {
        return itemService.getItemById(itemId, userId);
    }

    //Получение всех Item по userId
    @GetMapping
    public List<ItemDto> getAllItemByUser(@RequestHeader(USER_ID) Long userId) throws NotFoundException {
        return itemService.getAllItemByUser(userId);
    }

    //Получение Item по тексту
    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestHeader(USER_ID) Long userId, @RequestParam String text) {
        return itemService.getItemByText(userId, text);
    }
}
