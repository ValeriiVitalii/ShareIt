package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

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
    private final UserService userService;

    static final String USER_ID = "X-Sharer-User-Id";


    @PostMapping
    public ItemDto postItem(@RequestHeader(USER_ID) Long userId,
                            @Valid @RequestBody ItemDto itemDto) throws NotFoundException, ValidationException {
        userValidate(userId);

        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestHeader(USER_ID) Long userId, @PathVariable("itemId") Long itemId,
                             @Valid @RequestBody ItemDto item) throws NotFoundException {
        return itemService.editItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader(USER_ID) Long userId, @PathVariable("itemId") Long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader(USER_ID) Long userId) throws NotFoundException {
        return itemService.getAllItem(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItem(@RequestHeader(USER_ID) Long userId, @RequestParam String text) {
        return itemService.getItem(userId, text);
    }

    private void userValidate(Long userId) throws NotFoundException {
        userService.getUser(userId);
    }

}
