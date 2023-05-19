package ru.practicum.features.item;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.features.item.model.CommentDto;
import ru.practicum.features.item.model.ItemDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    static final String USER_ID = "X-Sharer-User-Id";

    //Создание Item
    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader(USER_ID) Long userId,
                                           @Valid @RequestBody ItemDto itemDto) {
        return itemClient.postItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader(USER_ID) Long userId,
                                              @PathVariable("itemId") Long itemId,
                                              @RequestBody CommentDto commentDto) {
        return itemClient.postComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader(USER_ID) Long userId, @PathVariable("itemId") Long itemId,
                                            @RequestBody ItemDto itemDto) {
        return itemClient.patchItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_ID) Long userId, @PathVariable("itemId") Long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    //Получение всех Item по userId
    @GetMapping
    public ResponseEntity<Object> getAllItemByUser(@RequestHeader(USER_ID) Long userId,
                                                   @RequestParam(required = false, defaultValue = "0") Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemClient.getAllItemByUser(userId, from, size);
    }

    //Получение Item по тексту
    @GetMapping("/search")
    public ResponseEntity<Object> getItemByText(@RequestHeader(USER_ID) Long userId,
                                                @RequestParam String text,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size) {
        return itemClient.getItemByText(userId, text, from, size);
    }
}
