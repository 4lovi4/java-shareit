package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByEndDesc(Long bookerId);
    List<Booking> findAllByBooker_IdAndStatusOrderByEndDesc(Long bookerId, BookingStatus status);
    List<Booking> findAllByItem_OwnerOrderByEndDesc(User owner);
    List<Booking> findAllByItem_OwnerAndStatusOrderByEndDesc(User owner, BookingStatus status);
    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start < :currentTime " +
            "AND b.end > :currentTime " +
            "order by b.end desc"
    )
    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByEndDesc(Long bookerId, LocalDateTime currentTime);
    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start < :currentTime " +
            "AND b.end < :currentTime " +
            "order by b.end desc")
    List<Booking> findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByEndDesc(Long bookerId, LocalDateTime currentTime);
    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start > :currentTime " +
            "AND b.end > :currentTime " +
            "order by b.end desc")
    List<Booking> findAllByBooker_IdAndStartAfterAndEndAfterOrderByEndDesc(Long bookerId, LocalDateTime currentTime);
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start < :currentTime " +
            "AND b.end > :currentTime " +
            "order by b.end desc")
    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByEndDesc(Long ownerId, LocalDateTime currentTime);
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start < :currentTime " +
            "AND b.end < :currentTime " +
            "order by b.end desc")
    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndBeforeOrderByEndDesc(Long ownerId, LocalDateTime currentTime);
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start > :currentTime " +
            "AND b.end > :currentTime " +
            "order by b.end desc")
    List<Booking> findAllByItem_Owner_IdAndStartAfterAndEndAfterOrderByEndDesc(Long ownerId, LocalDateTime currentTime);
    Optional<Booking> findFirstByItem_IdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime time);
    Optional<Booking> findFirstByItem_IdAndStartBeforeOrderByStartDesc(Long itemId, LocalDateTime time);
}
