package ru.practicum.features.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.features.request.model.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> postItemRequest(@RequestHeader(USER_ID) Long userId,
                                                  @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.postItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestWithItemsByCreator(@RequestHeader(USER_ID) Long userId) {
        return itemRequestClient.getItemRequestWithItemsByCreator(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestDtoByUserId(@RequestHeader(USER_ID) Long userId,
                                                            @PositiveOrZero @RequestParam(required = false,
                                                                    defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(required = false,
                                                                    defaultValue = "10") Integer size) {
        return itemRequestClient.getItemRequestDtoByUserId(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestDto(@RequestHeader(USER_ID) Long userId,
                                                    @PathVariable("requestId") Long requestId) {
        return itemRequestClient.getItemRequestDto(userId, requestId);
    }
}
