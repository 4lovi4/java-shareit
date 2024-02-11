package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.NearBookingDto;

@Setter
@Getter
@NoArgsConstructor
public class ItemWithBookingDto extends ItemDto {
    private NearBookingDto nextBooking;
    private NearBookingDto lastBooking;

    public ItemWithBookingDto(String name, String description, Boolean available) {
        super(name, description, available);
    }
}
