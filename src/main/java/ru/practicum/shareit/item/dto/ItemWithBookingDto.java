package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.NearBookingDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ItemWithBookingDto extends ItemDto {
    private NearBookingDto nextBooking;
    private NearBookingDto lastBooking;
    private List<CommentDto> comments;

    public ItemWithBookingDto(String name, String description, Boolean available) {
        super(name, description, available);
    }
}
