package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController("requestRestController")
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @Valid @RequestBody ItemRequestDto requestDto) {
        return itemRequestService.addItemRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsForUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @Valid @Min(value = 0, message = "from должен быть больше или равен 0")
                                               @RequestParam(name = "from", defaultValue = "0") Integer fromIndex,
                                               @Valid @Min(value = 1, message = "size не должен быть меньше 1")
                                               @RequestParam(defaultValue = "1") Integer size) {
        return itemRequestService.getAllRequests(userId, fromIndex, size).getContent();
    }

    @GetMapping("/{id}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("id") Long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
