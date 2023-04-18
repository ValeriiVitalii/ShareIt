package ru.practicum.shareit.features.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.features.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
