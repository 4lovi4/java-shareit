package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
        itemDto.setId(item.getId());
        return itemDto;
    }

    public static ItemWithBookingDto toItemWithBookingDto(Item item) {
        ItemWithBookingDto itemBookingDto = new ItemWithBookingDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
        itemBookingDto.setId(item.getId());
        return itemBookingDto;
    }

    public static Item toItem(ItemDto item) {
        return new Item(item.getName(),
                item.getDescription(),
                item.getAvailable());
    }
}
