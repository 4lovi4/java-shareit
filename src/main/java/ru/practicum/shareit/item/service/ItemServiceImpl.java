package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemWrongRequestException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.dto.ItemMapper.toItem;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private static final String USER_NOT_PROVIDED = "Не передан id пользователя";

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ItemDto> findAllItemsForUser(Long userId) {
        return itemRepository.findAllItemsForUser(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findItemById(Long userId, Long itemId) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        return toItemDto(itemRepository.findItemById(userId, itemId));
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        userRepository.findUserById(userId);
        return toItemDto(itemRepository.createItem(userId, toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        return toItemDto(itemRepository.updateItem(userId, itemId, toItem(itemDto)));
    }

    @Override
    public List<ItemDto> findItemsByText(Long userId, String searchText) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        return itemRepository.findItemsByText(searchText)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
