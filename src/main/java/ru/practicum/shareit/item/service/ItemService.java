package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> findAllItemsForUser(Long userId);

    ItemDto findItemById(Long userId, Long itemId);

    ItemDto createItem(Long userId, ItemDto item);

    ItemDto updateItem(Long userId, Long itemId, ItemDto item);

    List<ItemDto> findItemsByText(Long userId, String text);
}
