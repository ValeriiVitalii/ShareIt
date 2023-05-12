package ru.practicum.features.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.features.item.model.Item;
import ru.practicum.features.request.model.ItemAnswer;

import java.util.List;

public interface ItemAnswerRepository extends JpaRepository<ItemAnswer, Long> {
    @Query("SELECT ia.item FROM ItemAnswer ia WHERE ia.itemRequest.id = ?1")
    List<Item> findByItemRequestId(Long itemRequestId);
}
