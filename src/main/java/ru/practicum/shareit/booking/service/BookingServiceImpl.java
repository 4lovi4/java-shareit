package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingWrongRequestException;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemUnavailableException;
import ru.practicum.shareit.item.exception.ItemWrongRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.service.ItemServiceImpl.USER_NOT_PROVIDED;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingResponse addNewBooking(Long userId, BookingDto bookingDto) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        validateBooking(booking);
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findByIdAndOwner_IdNot(bookingDto.getItemId(), userId).orElseThrow(
                () -> new ItemNotFoundException(bookingDto.getItemId())
        );
        if (!item.getAvailable()) {
            throw new ItemUnavailableException(item.getId());
        }
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse changeBookingStatus(Long bookingId, Long userId, Boolean bookingApprove) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException(bookingId));
        if (!booking.getItem().getOwner().getId().equals(user.getId()) &&
        booking.getBooker().getId().equals(user.getId())) {
            throw new BookingNotFoundException(String.format("Вещь id %d не принадлежит пользователю %d, это хозяин брони",
                    booking.getItem().getId(), user.getId()));
        }
        if (!booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new BookingWrongRequestException(String.format("Вещь id %d не принадлежит пользователю %d",
                    booking.getItem().getId(), user.getId()));
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new BookingWrongRequestException(String.format("Для бронирования id %d уже установлен статус %s",
                    booking.getId(), booking.getStatus()));
        }
        if (Boolean.TRUE.equals(bookingApprove)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingResponse(bookingRepository.saveAndFlush(booking));
    }

    public BookingResponse getBookingById(Long bookingId, Long userId) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException(bookingId));
        if (!(userId.equals(booking.getBooker().getId()) ||
                userId.equals(booking.getItem().getOwner().getId()))) {
            throw new BookingNotFoundException(String.format("Бронирование %d не сов    ершалось пользователем id %d " +
                    "и вещь id %d не принадлежит ему", bookingId, userId, booking.getItem().getOwner().getId()));
        }
        return BookingMapper.toBookingResponse(booking);
    }

    @Override
    public Page<BookingResponse> getAllBookingsByBookerAndState(
            Long userId, BookingDtoState state, Integer fromIndex, Integer size) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        LocalDateTime currentTime = LocalDateTime.now();
        Page<Booking> bookings;
        Sort sort = Sort.by("end").descending();
        Pageable pageable = PageRequest.of(fromIndex, size, sort);
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_IdOrderByEndDesc(pageable, user.getId());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByEndDesc(pageable, user.getId(), currentTime);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBooker_IdAndStartAfterAndEndAfterOrderByEndDesc(pageable, user.getId(), currentTime);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByEndDesc(pageable, user.getId(), currentTime);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBooker_IdAndStatusOrderByEndDesc(pageable, user.getId(), BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBooker_IdAndStatusOrderByEndDesc(pageable, user.getId(), BookingStatus.REJECTED);
                break;
            default:
                throw new BookingWrongRequestException("Unknown state: " + state);
        }

        return bookings
                .map(BookingMapper::toBookingResponse);
    }

    @Override
    public Page<BookingResponse> getAllBookingsByOwnerAndState(
            Long ownerId, BookingDtoState state, Integer fromIndex, Integer size) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException(ownerId));
        LocalDateTime currentTime = LocalDateTime.now();
        Page<Booking> bookings;
        Sort sort = Sort.by("end").descending();
        Pageable pageable = PageRequest.of(fromIndex, size, sort);
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItem_OwnerOrderByEndDesc(pageable, owner);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndBeforeOrderByEndDesc(pageable, ownerId, currentTime);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartAfterAndEndAfterOrderByEndDesc(pageable, ownerId, currentTime);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByEndDesc(pageable, ownerId, currentTime);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItem_OwnerAndStatusOrderByEndDesc(pageable, owner, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItem_OwnerAndStatusOrderByEndDesc(pageable, owner, BookingStatus.REJECTED);
                break;
            default:
                throw new BookingWrongRequestException("Unknown state: " + state);
        }
        return bookings.map(BookingMapper::toBookingResponse);
    }

    private void validateBooking(Booking booking) {
        if (booking.getStart().isEqual(booking.getEnd())) {
            throw new BookingWrongRequestException("Время старта и окончания бронирования не могут быть равны");
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingWrongRequestException("Время окончания бронирования не может быть в прошлом");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingWrongRequestException("Время старта бронирования не может быть позже окончания");
        }

    }
}
