package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getAllRequests(Long userId);

    Page<ItemRequestDto> getAllRequestsPageable(Long userId, Integer fromIndex, Integer size);

    ItemRequestDto getRequestById(Long userId, Long requestId);
}
