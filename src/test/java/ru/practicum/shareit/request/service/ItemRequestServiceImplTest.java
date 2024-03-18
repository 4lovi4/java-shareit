package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Test
    void addItemRequestTest() {
        ItemRequestDto itemRequestDtoOne = ItemRequestDto.builder()
                .description("description 1")
                .build();
        User requester = User.builder()
                .id(100L)
                .email("abc@abc.ya")
                .name("abc")
                .build();
        ItemRequest requestToAdd = ItemRequestMapper.toItemRequest(itemRequestDtoOne);
        requestToAdd.setRequester(requester);
        Long expectedRequestId = 100L;
        LocalDateTime expectedRequestOneCreatedTime = LocalDateTime.of(2024, 3, 8, 12, 0, 0);
        ItemRequest addedItemRequest = ItemRequest.builder()
                .description(requestToAdd.getDescription())
                .id(expectedRequestId)
                .created(expectedRequestOneCreatedTime)
                .requester(requester)
                .build();
        when(requestRepository.saveAndFlush(requestToAdd)).thenReturn(addedItemRequest);
    }
}
