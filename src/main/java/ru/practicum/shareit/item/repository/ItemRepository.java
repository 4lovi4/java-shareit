package ru.practicum.shareit.item.repository;


import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> findAllItemsForUser(Long userId);

    Item findItemById(Long userId, Long itemId);

    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item item);

    List<Item> findItemsByText(String text);
}
