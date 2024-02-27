package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    List<ItemWithBookingDto> findAllItemsForUser(Long userId);

    ItemDto findItemById(Long userId, Long itemId);

    ItemDto createItem(Long userId, ItemDto item);

    ItemDto updateItem(Long userId, Long itemId, ItemDto item);

    List<ItemDto> findItemsByText(Long userId, String text);
    CommentDto addCommentForItem(Long userId, Long itemId, CommentDto comment);
}
