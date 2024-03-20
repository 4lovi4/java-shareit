package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl requestService;

    @Test
    void addNewItemRequestTest() {
        ItemRequestDto itemRequestDtoToAdd = ItemRequestDto.builder()
                .description("description 1")
                .build();
        User requester = User.builder()
                .id(100L)
                .email("abc@abc.ya")
                .name("abc")
                .build();
        ItemRequest requestToAdd = ItemRequestMapper.toItemRequest(itemRequestDtoToAdd);
        requestToAdd.setRequester(requester);
        Long expectedRequestId = 100L;
        LocalDateTime createTime = LocalDateTime.of(2024, 3, 8, 12, 0, 0);
        ItemRequest requestAdded = ItemRequest.builder()
                .description(requestToAdd.getDescription())
                .id(expectedRequestId)
                .requester(requester)
                .created(createTime)
                .build();

        when(userRepository.findById(requester.getId())).thenReturn(Optional.of(requester));
        when(requestRepository.saveAndFlush(any(ItemRequest.class))).thenReturn(requestAdded);
        ItemRequestDto itemRequestDtoAdded = requestService.addItemRequest(requester.getId(), itemRequestDtoToAdd);

        verify(requestRepository, times(1)).saveAndFlush(any(ItemRequest.class));
        assertThat(itemRequestDtoAdded.getId(), is(equalTo(expectedRequestId)));
        assertThat(itemRequestDtoAdded.getDescription(), is(equalTo(itemRequestDtoToAdd.getDescription())));
        assertThat(itemRequestDtoAdded.getRequester(), is(requester.getId()));
        assertThat(itemRequestDtoAdded.getCreated(), is(equalTo(createTime)));
    }

    @Test
    void addNewItemRequestShouldThrowExceptionUserNotFound() {
        ItemRequestDto itemRequestDtoToAdd = ItemRequestDto.builder()
                .description("description not found user")
                .build();
        Long wrongUserId = 1000L;
        String expectedExceptionMessage = String.format("Пользователь id = %d не найден", wrongUserId);

        when(userRepository.findById(wrongUserId)).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> requestService.addItemRequest(wrongUserId, itemRequestDtoToAdd));
        assertThat(userNotFoundException.getMessage(), is(equalTo(expectedExceptionMessage)));
    }
}
