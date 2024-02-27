package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NearBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemWrongRequestException;
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
    public static final String USER_NOT_PROVIDED = "Не передан id пользователя";

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
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
                .findFirstByItem_IdAndItem_Owner_IdAndStartAfterOrderByStartAsc(item.getId(), user.getId(), currentTime);
        Optional<Booking> lastBooking = bookingRepository
                .findFirstByItem_IdAndItem_Owner_IdAndStartBeforeOrderByStartDesc(item.getId(), user.getId(), currentTime);
        ItemWithBookingDto itemDto = toItemWithBookingDto(item);
        nextBooking.ifPresent(booking -> {
            NearBookingDto nextNearBookingDto = toNearBooking(booking);
            itemDto.setNextBooking(nextNearBookingDto);
        });
        lastBooking.ifPresent(booking -> {
            NearBookingDto lastNearBookingDto = toNearBooking(booking);
            itemDto.setLastBooking(lastNearBookingDto);
        });
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
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
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
    public CommentDto addCommentForItem(Long userId, Long itemId, CommentDto comment) {
        return new CommentDto();
    }
}
