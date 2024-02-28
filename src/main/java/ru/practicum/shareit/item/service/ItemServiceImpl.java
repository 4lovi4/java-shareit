package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NearBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemWrongRequestException;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import static ru.practicum.shareit.booking.dto.BookingMapper.toNearBooking;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.dto.ItemMapper.toItem;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemWithBookingDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    public static final String USER_NOT_PROVIDED = "Не передан id пользователя";

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<ItemWithBookingDto> findAllItemsForUser(Long userId) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        LocalDateTime currentTime = LocalDateTime.now();
        User owner = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
        return itemRepository.findByOwnerOrderByIdAsc(owner)
                .stream().map(it ->
                {
                    ItemWithBookingDto itWithBooking = ItemMapper.toItemWithBookingDto(it);
                    Set<Booking> itBookings = it.getBookings();
                    if (itBookings.isEmpty()) {
                        return itWithBooking;
                    }
                    Booking lastBooking = itBookings.stream()
                            .filter(itBooking -> itBooking.getStart().isBefore(currentTime))
                            .max(Comparator.comparing(Booking::getStart))
                            .orElse(null);
                    Booking nextBooking = itBookings.stream()
                            .filter(itBooking -> itBooking.getStart().isAfter(currentTime))
                            .min(Comparator.comparing(Booking::getStart))
                            .orElse(null);
                    NearBookingDto lastNearBooking = null;
                    if (lastBooking != null) {
                        lastNearBooking = BookingMapper.toNearBooking(lastBooking);
                    }
                    itWithBooking.setLastBooking(lastNearBooking);
                    NearBookingDto nextNearBooking = null;
                    if (nextBooking != null) {
                        nextNearBooking = BookingMapper.toNearBooking(nextBooking);
                    }
                    itWithBooking.setNextBooking(nextNearBooking);
                    List<Comment> itComments = new ArrayList<>(it.getComments());
                    itComments.sort(Comparator.comparing(Comment::getId));
                    itWithBooking.setComments(itComments.stream()
                            .map(comment -> {
                                CommentDto commentDto = CommentMapper.toCommentDto(comment);
                                commentDto.setAuthorName(comment.getUser().getName());
                                return commentDto;
                            })
                            .sorted(Comparator.comparing(CommentDto::getId))
                            .collect(Collectors.toList()));
                    return itWithBooking;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemWithBookingDto findItemById(Long userId, Long itemId) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        LocalDateTime currentTime = LocalDateTime.now();
        Optional<Booking> nextBooking = bookingRepository
                .findFirstByItem_IdAndItem_Owner_IdAndStatusNotAndStartAfterOrderByStartAsc(item.getId(),
                        user.getId(),
                        BookingStatus.REJECTED,
                        currentTime);
        Optional<Booking> lastBooking = bookingRepository
                .findFirstByItem_IdAndItem_Owner_IdAndStatusNotAndStartBeforeOrderByStartDesc(item.getId(),
                        user.getId(),
                        BookingStatus.REJECTED,
                        currentTime);
        ItemWithBookingDto itemDto = toItemWithBookingDto(item);
        nextBooking.ifPresent(booking -> {
            NearBookingDto nextNearBookingDto = toNearBooking(booking);
            itemDto.setNextBooking(nextNearBookingDto);
        });
        lastBooking.ifPresent(booking -> {
            NearBookingDto lastNearBookingDto = toNearBooking(booking);
            itemDto.setLastBooking(lastNearBookingDto);
        });

        List<Comment> itemComments = new ArrayList<>(item.getComments());
        itemComments.sort(Comparator.comparing(Comment::getId));
        itemDto.setComments(itemComments.stream()
                .map(comment -> {
                    CommentDto commentDto = CommentMapper.toCommentDto(comment);
                    commentDto.setAuthorName(comment.getUser().getName());
                    return commentDto;
                })
                .sorted(Comparator.comparing(CommentDto::getId))
                .collect(Collectors.toList()));
        return itemDto;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        User owner = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
        Item newItem = toItem(itemDto);
        newItem.setOwner(owner);
        return toItemDto(itemRepository.save(newItem));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Item itemStored = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(itemId));
        if (Objects.nonNull(itemDto.getName())) {
            itemStored.setName(itemDto.getName());
        }
        if (Objects.nonNull(itemDto.getDescription())) {
            itemStored.setDescription(itemDto.getDescription());
        }
        if (Objects.nonNull(itemDto.getAvailable())) {
            itemStored.setAvailable(itemDto.getAvailable());
        }
        return toItemDto(itemRepository.save(itemStored));
    }

    @Override
    public List<ItemDto> findItemsByText(Long userId, String searchText) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        if (searchText.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameOrDescriptionContainingIgnoreCase(searchText)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addCommentForItem(Long userId, Long itemId, CommentDto commentDto) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        LocalDateTime currentTime = LocalDateTime.now();
        if (bookingRepository.findAllByItem_Id(itemId)
                .stream()
                .noneMatch(b -> b.getBooker().getId().equals(userId) &&
                        b.getEnd().isBefore(currentTime) &&
                        b.getStatus().equals(BookingStatus.APPROVED))
        ) {
            throw new ItemWrongRequestException(
                    String.format("У пользователя id %d нет законченных бронирований вещи id %d.",
                            userId, itemId));
        }
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setUser(user);
        comment.setItem(item);
        comment.setCreatedTime(currentTime);
        CommentDto commentDtoResponse = CommentMapper.toCommentDto(commentRepository.saveAndFlush(comment));
        commentDtoResponse.setAuthorName(user.getName());
        return commentDtoResponse;
    }
}
