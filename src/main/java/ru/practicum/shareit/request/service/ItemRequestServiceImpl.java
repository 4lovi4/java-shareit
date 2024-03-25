package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemResponse;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private ItemRequestRepository itemRequestRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserRepository userRepository,
                                  ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto addItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        ItemRequest request = ItemRequestMapper.toItemRequest(itemRequestDto);
        LocalDateTime currentTime = LocalDateTime.now();
        request.setRequester(requester);
        request.setCreated(currentTime);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.saveAndFlush(request));
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        List<ItemRequest> requestsByReporter = itemRequestRepository
                .findAllByRequester_IdOrderByCreatedDesc(requester.getId());
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        List<Long> requestsIdByReporter = requestsByReporter
                .stream()
                .map(r -> r.getRequester().getId())
                .collect(Collectors.toList());
        if (requestsIdByReporter.isEmpty()) {
            return itemRequestDtos;
        }
        List<Item> itemsByRequestsId = itemRepository.findAllByRequest_IdIn(requestsIdByReporter);
        itemRequestDtos = requestsByReporter
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .map(r -> {
                    List<ItemResponse> items = itemsByRequestsId
                            .stream()
                            .filter(i -> i.getRequest().getId().equals(r.getId()))
                            .map(ItemRequestMapper::toItemResponse)
                            .sorted(Comparator.comparing(ItemResponse::getId))
                            .collect(Collectors.toList());
                    r.setItems(items);
                    return r;
                })
                .collect(Collectors.toList());
        return itemRequestDtos;
    }

    @Override
    public Page<ItemRequestDto> getAllRequestsPageable(Long userId, Integer fromIndex, Integer size) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Sort sort = Sort.by("created").descending();
        Pageable pageable = PageRequest.of(fromIndex, size, sort);
        Page<ItemRequest> pageRequests = itemRequestRepository.findAllByRequester_IdNot(pageable, userId);
        List<Long> requestsId = pageRequests
                .getContent()
                .stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        List<ItemResponse> requestsItems = itemRepository.findAllByRequest_IdIn(requestsId)
                .stream()
                .map(ItemRequestMapper::toItemResponse)
                .collect(Collectors.toList());
        return pageRequests
                .map(ItemRequestMapper::toItemRequestDto)
                .map(r -> {
                    r.setItems(requestsItems
                            .stream()
                            .filter(i -> i.getRequestId().equals(r.getId()))
                            .collect(Collectors.toList()));
                    return r;
                });
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new RequestNotFoundException(requestId)
        );
        ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(request);
        List<ItemResponse> itemsByRequestsId = itemRepository
                .findAllByRequest_IdIn(List.of(new Long[]{request.getId()}))
                .stream()
                .map(ItemRequestMapper::toItemResponse)
                .collect(Collectors.toList());
        requestDto.setItems(itemsByRequestsId);
        return requestDto;
    }
}
