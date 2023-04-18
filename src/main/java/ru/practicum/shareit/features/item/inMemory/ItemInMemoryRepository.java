package ru.practicum.shareit.features.item.inMemory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.features.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemInMemoryRepository implements ItemInMemory {

    private long itemId = 1;

    Map<Long, Item> items = new HashMap<>();

    @Override
    public Item addItem(Long userId, Item item) {
        if (item.getId() == null) {
            item.setId(itemId++);
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItem() {
        return new ArrayList<>(items.values());
    }
}
