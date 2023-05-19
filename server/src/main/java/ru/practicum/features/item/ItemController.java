package ru.practicum.features.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.features.item.model.CommentDto;
import ru.practicum.features.item.model.ItemDto;
import ru.practicum.features.item.service.ItemService;
import ru.practicum.exceptions.*;

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
                            @RequestBody ItemDto itemDto) throws NotFoundException, ValidationException {
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
                             @RequestBody ItemDto itemDto) throws NotFoundException {
        return itemService.editItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(USER_ID) Long userId, @PathVariable("itemId") Long itemId) throws NotFoundException {
        return itemService.getItemById(itemId, userId);
    }

    //Получение всех Item по userId
    @GetMapping
    public List<ItemDto> getAllItemByUser(@RequestHeader(USER_ID) Long userId,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size) throws NotFoundException, ValidationException {
        return itemService.getAllItemByUser(userId, from, size);
    }

    //Получение Item по тексту
    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestHeader(USER_ID) Long userId,
                                       @RequestParam String text,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "100") Integer size) throws ValidationException {
        return itemService.getItemByText(userId, text, from, size);
    }
}
