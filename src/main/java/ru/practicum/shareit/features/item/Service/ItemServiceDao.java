package ru.practicum.shareit.features.item.Service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.features.booking.BookingRepository;
import ru.practicum.shareit.features.booking.BookingStatus;
import ru.practicum.shareit.features.booking.model.Booking;
import ru.practicum.shareit.features.booking.model.BookingShortIdWithBookerId;
import ru.practicum.shareit.features.item.CommentRepository;
import ru.practicum.shareit.features.item.ItemRepository;
import ru.practicum.shareit.features.item.model.*;
import ru.practicum.shareit.features.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@AllArgsConstructor
public class ItemServiceDao implements ItemService {

    private final ItemRepository itemRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final UserService userService;

    @Override
    public ItemDto postItem(Long userId, ItemDto itemDto) throws ValidationException, NotFoundException {
        validate(itemDto);
        containsUser(userId);
        Item itemWithId = itemRepository.save(toItem(itemDto, userId));

        log.info("Добавлен новая вещь c ID={}", itemWithId.getId());
        return toItemDto(itemWithId);

    }

    @Override
    public CommentDto postComment(Long userId, Long itemId, CommentDto commentDto) throws NotFoundException, ValidationException {
        commentDto.setAuthorName(userService.getUserById(userId).getName());
        validationComment(userId, itemId, commentDto);
        Comment comment = commentRepository.save(toComment(commentDto, itemId, userId));

        log.info("Добавлен новый коментарий c ID={}", comment.getId());
        return toCommentDto(comment, userId);
    }

    @Override
    public ItemDto editItem(Long userId, Long id, ItemDto updateItem) throws NotFoundException {
        userValidate(userId);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с айди:" + id + " не найдена!"));

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
        return toItemDto(itemRepository.save(item));
    }

    //Получение ItemDto по itemId
    @Override
    public ItemDto getItemById(Long itemId, Long userId) throws NotFoundException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с айди:" + itemId + " не найдена!"));
        ItemDto itemDto = toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с айди:" + itemId + " не найдена!")));
        setComments(itemDto);

        if (item.getOwner().getId().equals(userId)) {
            return setLastAndNextBooking(itemDto);
        }
        return itemDto;
    }

    //Получение Item по itemId
    @Override
    public Item getItem(Long itemId) throws NotFoundException {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с айди:" + itemId + " не найдена!"));
    }

    @Override
    public ItemShortIdWithName getItemShortIdWithNameById(Long itemId) throws NotFoundException {
        return toItemShortIdWithName(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с айди:" + itemId + " не найдена!")));
    }

    //Получение всех Item по userId
    @Override
    public List<ItemDto> getAllItemByUser(Long userId) throws NotFoundException {
        userValidate(userId);
        List<ItemDto> userItems = new ArrayList<>();

        itemRepository.findAll().stream()
                .filter(i -> i.getOwner().getId().equals(userId))
                .forEach(i -> {
                    try {
                        userItems.add(setLastAndNextBooking(toItemDto(
                                itemRepository.findById(i.getId()).orElseThrow())));
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
        return userItems;
    }

    //Получение Item по тексту
    @Override
    public List<ItemDto> getItemByText(Long userId, String text) {
        List<ItemDto> foundItems = new ArrayList<>();
        itemRepository.findAll().stream()
                .filter(i -> findWord(i.getDescription(), text) && i.getAvailable())
                .forEach(i -> foundItems.add(toItemDto(itemRepository.findById(i.getId()).orElseThrow())));
        return foundItems;
    }

    @Override
    public void userValidate(Long userId) throws NotFoundException {
        boolean checkUser = itemRepository.findAll().stream()
                .anyMatch(i -> i.getOwner().getId().equals(userId));
        if (!checkUser) {
            throw new NotFoundException("У этого пользователя нет вещей!");
        }
    }

    private ItemDto setLastAndNextBooking(ItemDto itemDto) throws NotFoundException {
        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStartAsc(itemDto.getId());
        if (!bookings.isEmpty()) {
            Optional<Booking> lastBookings = bookings.stream()
                    .filter(b -> !b.getStatus().equals(BookingStatus.REJECTED)
                            && b.getStart().isBefore(LocalDateTime.now()))
                    .reduce((first, second) -> second);
            lastBookings.ifPresent(b -> {
                try {
                    itemDto.setLastBooking(toBookingShortIdWithBookerId(b));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            Optional<Booking> nextBooking = bookings.stream()
                    .filter(b -> !b.getStatus().equals(BookingStatus.REJECTED)
                            && b.getStart().isAfter(LocalDateTime.now()))
                    .findFirst();
            nextBooking.ifPresent(b -> {
                try {
                    itemDto.setNextBooking(toBookingShortIdWithBookerId(b));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return itemDto;
    }

    public void setComments(ItemDto itemDto) {
        List<Comment> comments = commentRepository.findCommentsByItemId(itemDto.getId());
        if (!comments.isEmpty()) {
            itemDto.setComments(toCommentsDto(comments));
            return;
        }
        itemDto.setComments(new ArrayList<>());
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

    private void validationComment(Long userId, Long itemId, CommentDto commentDto) throws ValidationException {
        List<Booking> bookings = bookingRepository.findByBookerAndItem(userId, itemId, BookingStatus.APPROVED);
        if (commentDto.getText().isEmpty()) {
            throw new ValidationException("Вы не написали свой коментарий!");
        }
        if (bookings.isEmpty()) {
            throw new ValidationException("Вы не брали эту вещь в аренду!");
        }
        if (bookings.get(0).getStart().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Аренда еще не началась");
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

    private BookingShortIdWithBookerId toBookingShortIdWithBookerId(Booking booking) throws NotFoundException {
        return BookingShortIdWithBookerId.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    private Comment toComment(CommentDto commentDto, Long itemId, Long userId) throws NotFoundException {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(getItem(itemId))
                .user(userService.getUserById(userId))
                .build();

    }

    private List<CommentDto> toCommentsDto(List<Comment> comments) {

        return comments.stream()
                .map(c -> {
                    try {
                        return toCommentDto(c, c.getUser().getId());
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private CommentDto toCommentDto(Comment comment, Long userId) throws NotFoundException {
        return CommentDto.builder()
                .id(comment.getId())
                .created(LocalDateTime.now())
                .text(comment.getText())
                .authorName(userService.getUserById(userId).getName())
                .build();
    }
}
