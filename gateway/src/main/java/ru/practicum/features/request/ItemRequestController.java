package ru.practicum.features.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exceptions.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    /*private final ItemRequestService itemRequestService;

    static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto postItemRequest(@RequestHeader(USER_ID) Long userId,
                                          @Valid @RequestBody ItemRequestDto itemRequestDto) throws NotFoundException, ValidationException {
        return itemRequestService.postItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestWithItems> getItemRequestWithItemsByCreator(@RequestHeader(USER_ID) Long userId) throws NotFoundException {
        return itemRequestService.getItemRequestWithItemsByCreator(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithItems> getItemRequestDtoByUserId(@RequestHeader(USER_ID) Long userId,
                                                                @RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(defaultValue = "100") Integer size) throws NotFoundException, ValidationException {
        return itemRequestService.getItemRequestWithItemsUserId(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItems getItemRequestDto(@RequestHeader(USER_ID) Long userId,
                                                                 @PathVariable("requestId") Long requestId) throws NotFoundException {
       return itemRequestService.getItemRequestWithItemsById(userId, requestId);
    }*/
}
