package ru.practicum.features.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.features.item.model.Item;


public interface ItemRepository extends JpaRepository<Item, Long> {
}
