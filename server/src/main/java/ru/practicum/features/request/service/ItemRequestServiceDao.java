package ru.practicum.features.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.features.item.model.Item;
import ru.practicum.features.item.model.ItemDto;
import ru.practicum.features.request.model.ItemRequest;
import ru.practicum.features.request.model.ItemRequestDto;
import ru.practicum.features.request.model.ItemRequestWithItems;
import ru.practicum.features.request.repository.ItemAnswerRepository;
import ru.practicum.features.request.repository.ItemRequestRepository;
import ru.practicum.features.user.service.UserService;
import ru.practicum.exceptions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemRequestServiceDao implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final ItemAnswerRepository itemAnswerRepository;

    private final UserService userService;

    @Override
    public ItemRequestDto postItemRequest(Long userId, ItemRequestDto itemRequestDto) throws NotFoundException, ValidationException {
        validationUser(userId);
        validationDescription(itemRequestDto.getDescription());
        ItemRequest itemRequestWithId = itemRequestRepository.save(toItemRequest(userId, itemRequestDto));

        log.info("Добавлена новый ItemRequest с ID={}", itemRequestWithId.getId());
        return toItemRequestDto(itemRequestWithId);
    }

    @Override
    public List<ItemRequestWithItems> getItemRequestWithItemsByCreator(Long userId) throws NotFoundException {
        validationUser(userId);

        List<ItemRequestWithItems> itemRequestsWithItems =
                toItemRequestsWithItems(itemRequestRepository.findAllByCreator(userService.getUserById(userId)));
        return setItemsAnswer(itemRequestsWithItems);
    }

    @Override
    public List<ItemRequestWithItems> getItemRequestWithItemsUserId(Long userId, Integer from, Integer size) throws NotFoundException, ValidationException {
        validationUser(userId);
        PageRequest pageable = getPageRequest(from, size);

        List<ItemRequestWithItems> itemRequestsWithItems =
                toItemRequestsWithItems(itemRequestRepository.findAllItemRequestExceptCreator(userId, pageable));
        return setItemsAnswer(itemRequestsWithItems);
    }

    @Override
    public ItemRequestWithItems getItemRequestWithItemsById(Long userId, Long requestId) throws NotFoundException {
        validationUser(userId);

        if (itemRequestRepository.findItemRequestById(requestId) == null) {
            throw new NotFoundException("Запрос не найден!");
        }
        ItemRequestWithItems itemRequestWithItems = toItemRequestWithItems(itemRequestRepository.findItemRequestById(requestId));
        itemRequestWithItems.setItems(toItemsDto(itemAnswerRepository.findByItemRequestId(
                itemRequestWithItems.getId()), itemRequestWithItems.getId()));

        return itemRequestWithItems;
    }

    private List<ItemRequestWithItems> setItemsAnswer(List<ItemRequestWithItems> itemRequestWithItems) {
        itemRequestWithItems.forEach(i -> {
            try {
                i.setItems(toItemsDto(itemAnswerRepository.findByItemRequestId(i.getId()), i.getId()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return itemRequestWithItems;
    }

    private void validationUser(Long userId) throws NotFoundException {
        userService.getUserById(userId);
    }

    private void validationDescription(String description) throws ValidationException {
        if (description == null) {
            throw new ValidationException("Описание не может быть пустым");
        }
    }

    private PageRequest getPageRequest(Integer from, Integer size) throws ValidationException {
        if (from < 0) {
            throw new ValidationException("Некорректное значение from = " + from);
        }
        return PageRequest.of(from / size, size);
    }

    private ItemRequest toItemRequest(Long userId, ItemRequestDto itemRequestDto) throws NotFoundException {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .creator(userService.getUserById(userId))
                .created(LocalDateTime.now())
                .build();
    }

    private ItemRequestDto toItemRequestDto(ItemRequest itemRequest) throws NotFoundException {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    private ItemRequestWithItems toItemRequestWithItems(ItemRequest itemRequest) throws NotFoundException {
        return ItemRequestWithItems.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(new ArrayList<>())
                .build();
    }

    private ItemDto toItemDto(Item item, Long requestId) throws NotFoundException {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .build();
    }

    private List<ItemRequestWithItems> toItemRequestsWithItems(List<ItemRequest> itemRequests) {
        List<ItemRequestWithItems> itemRequestsWithItems = new ArrayList<>();
        itemRequests.forEach(i -> {
            try {
                itemRequestsWithItems.add(toItemRequestWithItems(i));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return itemRequestsWithItems;
    }

    private List<ItemDto> toItemsDto(List<Item> items, Long requestId) throws NotFoundException {
        List<ItemDto> itemsDto = new ArrayList<>();
        items.forEach(i -> {
            try {
                itemsDto.add(toItemDto(i, requestId));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return itemsDto;
    }
}
