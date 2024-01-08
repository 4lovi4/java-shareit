package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Long, List<Item>> items;
    private Long itemsCounter;

    public ItemRepositoryImpl() {
        this.items = new HashMap<>();
        itemsCounter = 0L;
    }

    @Override
    public List<Item> findAllItemsForUser(Long userId) {
        List<Item> userItems = items.get(userId);
        return Objects.isNull(userItems) ?
                new ArrayList<>() :
                userItems;
    }

    @Override
    public Item findItemById(Long userId, Long itemId) throws ItemNotFoundException {
        List<Item> allItems = getAllUsersItems();
        Optional<Item> itemById = allItems.stream()
                .filter(it -> Objects.equals(it.getId(), itemId))
                .findAny();
        if (itemById.isEmpty()) {
            throw new ItemNotFoundException(itemId);
        }
        return itemById.get();
    }

    @Override
    public Item createItem(Long userId, Item item) {
        Long itemId = updateItemsCounter();
        item.setId(itemId);
        items.computeIfAbsent(userId, it -> new ArrayList<>());
        item.setOwner(userId);
        items.get(userId).add(item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        Optional<Item> itemOptional = findAllItemsForUser(userId)
                .stream()
                .filter(it -> itemId.equals(it.getId())).findAny();
        if (itemOptional.isEmpty()) {
            throw new ItemNotFoundException(itemId);
        }
        Item itemPresent = itemOptional.get();
        if (!Objects.isNull(item.getName())) {
            itemPresent.setName(item.getName());
        }
        if (!Objects.isNull(item.getDescription())) {
            itemPresent.setDescription(item.getDescription());
        }
        if (!Objects.isNull(item.getOwner())) {
            itemPresent.setOwner(item.getOwner());
        }
        if (!Objects.isNull(item.getRequest())) {
            itemPresent.setRequest(item.getRequest());
        }
        if (!Objects.isNull(item.getAvailable())) {
            itemPresent.setAvailable(item.getAvailable());
        }
        return itemPresent;
    }

    @Override
    public List<Item> findItemsByText(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (!text.isBlank()) {
            List<Item> allItems = getAllUsersItems();
            searchItems = allItems.stream()
                    .filter(item -> (item.getName().toLowerCase()
                            .contains(text.toLowerCase()) ||
                            item.getDescription().toLowerCase()
                                    .contains(text.toLowerCase())) &&
                            item.getAvailable()
                    )
                    .collect(Collectors.toList());
        }
        return searchItems;
    }

    private Long updateItemsCounter() {
        return ++itemsCounter;
    }

    private List<Item> getAllUsersItems() {
        return items
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
