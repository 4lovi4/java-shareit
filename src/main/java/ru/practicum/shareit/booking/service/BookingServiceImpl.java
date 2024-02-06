package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingWrongRequestException;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemWrongRequestException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.service.ItemServiceImpl.USER_NOT_PROVIDED;

@Service
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
    public BookingDto addNewBooking(Long userId, BookingDto bookingDto) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        validateBooking(booking);
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto changeBookingStatus(Long bookingId, Long userId, Boolean bookingApprove) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException(bookingId));
        if (!booking.getBooker().getId().equals(userId)) {
            throw new BookingWrongRequestException(String.format("Бронирование id %d не принадлежит пользователю %d",
                    bookingId, userId));
        }
        if (Boolean.TRUE.equals(bookingApprove)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    public BookingDto getBookingById(Long bookingId, Long userId) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException(bookingId));

        if (userId.equals(booking.getBooker().getId()) &&
                userId.equals(booking.getItem().getOwner().getId())) {
            new BookingWrongRequestException(String.format("Пользователь id %d не совершал данное " +
                    "бронирование и не являетя хозяином вещи", userId));
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByBookerWithStatusAndState(Long userId, BookingStatus status, BookingDtoState state) {
        if (Objects.isNull(userId)) {
            throw new ItemWrongRequestException(USER_NOT_PROVIDED);
        }
        LocalDateTime currentTime = LocalDateTime.now();
        ArrayList<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings.addAll(bookingRepository.findAllByBooker_Id(userId));
                break;
            case PAST:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBefore(userId, currentTime));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStartAfterAndEndAfter(userId, currentTime));
                break;
            case CURRENT:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(userId, currentTime));
                break;
            case WAITING:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStatus(userId, BookingStatus.WAITING));
                break;
            case REJECTED:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStatus(userId, BookingStatus.REJECTED));
                break;
            default:
                throw new BookingWrongRequestException("Неправильные параметры запроса подтверждения бронирования");
        }

        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart)
                        .thenComparing(Booking::getEnd))
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerAndState(Long ownerId, BookingDtoState state) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(UserNotFoundException::new);
        LocalDateTime currentTime = LocalDateTime.now();
        ArrayList<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings.addAll(bookingRepository.findAllByItem_Owner(owner));
                break;
            case PAST:
                bookings.addAll(bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndBefore(ownerId, currentTime));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findAllByItem_Owner_IdAndStartAfterAndEndAfter(ownerId, currentTime));
                break;
            case CURRENT:
                bookings.addAll(bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(ownerId, currentTime));
                break;
            case WAITING:
                bookings.addAll(bookingRepository.findAllByItem_OwnerAndStatus(owner, BookingStatus.WAITING));
                break;
            case REJECTED:
                bookings.addAll(bookingRepository.findAllByItem_OwnerAndStatus(owner, BookingStatus.REJECTED));
                break;
            default:
                throw new BookingWrongRequestException("Неправильные параметры запроса подтверждения бронирования");
        }
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart)
                        .thenComparing(Booking::getEnd))
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void validateBooking(Booking booking) {
        if (booking.getStart().isEqual(booking.getEnd())) {
            throw new BookingWrongRequestException("Время старта и окончания бронирования не может быть равно");
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new BookingWrongRequestException("Время старта бронирования не может быть позже окончания");
        }
    }
}
