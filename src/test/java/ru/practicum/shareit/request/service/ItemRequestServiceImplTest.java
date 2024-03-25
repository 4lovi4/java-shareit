package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@Slf4j
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
    void addNewItemRequestByWrongUserShouldThrowException() {
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

    static Stream<Arguments> provideAllRequestsForUserSource() {
        User requester = User.builder()
                .id(100L)
                .name("user100")
                .email("user100@yandex.il")
                .build();
        User owner = User.builder()
                .id(101L)
                .name("user101")
                .email("user101@yandex.il")
                .build();
        ItemRequest requestOne = ItemRequest.builder()
                .id(1L)
                .description("запрос вещи 1")
                .created(LocalDateTime.of(2024, 3, 8, 12, 0, 0))
                .requester(requester)
                .build();
        ItemRequest requestTwo = ItemRequest.builder()
                .id(2L)
                .description("запрос вещи 2")
                .created(LocalDateTime.of(2024, 3, 9, 12, 0, 0))
                .requester(requester)
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("вещь 1")
                .description("описание 1")
                .available(true)
                .request(requestOne)
                .build();
        ItemRequestDto requestDtoOne = ItemRequestDto.builder()
                .id(requestOne.getId())
                .created(requestOne.getCreated())
                .requester(requester.getId())
                .description(requestOne.getDescription())
                .items(List.of(ItemResponse.builder()
                        .id(itemOne.getId())
                        .name(itemOne.getName())
                        .description(itemOne.getDescription())
                        .available(itemOne.getAvailable())
                        .requestId(requestOne.getId())
                        .build()))
                .build();

        return Stream.of(
                Arguments.of(requester, List.of(requestOne), List.of(itemOne), List.of(requestDtoOne)));
    }

    @ParameterizedTest
    @MethodSource("provideAllRequestsForUserSource")
    void getAllRequestShouldReturnCollection(User user,
                                             List<ItemRequest> storedRequests,
                                             List<Item> storedItems,
                                             List<ItemRequestDto> expectedResponseRequests) {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequester_IdOrderByCreatedDesc(anyLong()))
                .thenReturn(storedRequests);
        when(itemRepository.findAllByRequest_IdIn(anyList()))
                .thenReturn(storedItems);
        List<ItemRequestDto> allRequestsResponse = requestService.getAllRequests(user.getId());
        log.info("expectedResponseRequests: {}", expectedResponseRequests.get(0));
        log.info("allRequestsResponse: {}", allRequestsResponse.get(0));
        assertThat(allRequestsResponse, contains(expectedResponseRequests.toArray()));
    }

    @Test
    void getAllRequestShouldReturnEmptyCollection() {
        Long userId = 100L;
        User user = User.builder()
                .id(userId)
                .name("user")
                .email("user@yandex.il")
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequester_IdOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of());

        List<ItemRequestDto> allRequestsResponse = requestService.getAllRequests(userId);
        assertThat(allRequestsResponse, emptyIterable());
    }

    @Test
    void getAllRequestsByWrongUserShouldThrowException() {
        Long wrongUserId = 1000L;
        String expectedExceptionMessage = String.format("Пользователь id = %d не найден", wrongUserId);
        when(userRepository.findById(wrongUserId)).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> requestService.getAllRequests(wrongUserId));
        assertThat(userNotFoundException.getMessage(), is(equalTo(expectedExceptionMessage)));
    }
}
