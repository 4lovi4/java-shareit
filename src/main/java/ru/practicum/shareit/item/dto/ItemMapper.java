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
        itemDto.setRequestId(item.getRequest().getId());
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

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }
}
