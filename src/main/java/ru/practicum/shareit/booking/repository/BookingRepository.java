package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBooker_IdOrderByEndDesc(Pageable pageable, Long bookerId);

    Page<Booking> findAllByBooker_IdAndStatusOrderByEndDesc(Pageable pageable, Long bookerId, BookingStatus status);

    Page<Booking> findAllByItem_OwnerOrderByEndDesc(Pageable pageable, User owner);

    Page<Booking> findAllByItem_OwnerAndStatusOrderByEndDesc(Pageable pageable, User owner, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start < :currentTime " +
            "AND b.end > :currentTime " +
            "order by b.end desc"
    )
    Page<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByEndDesc(Pageable pageable, Long bookerId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start < :currentTime " +
            "AND b.end < :currentTime " +
            "order by b.end desc")
    Page<Booking> findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByEndDesc(Pageable pageable, Long bookerId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start > :currentTime " +
            "AND b.end > :currentTime " +
            "order by b.end desc")
    Page<Booking> findAllByBooker_IdAndStartAfterAndEndAfterOrderByEndDesc(Pageable pageable, Long bookerId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start < :currentTime " +
            "AND b.end > :currentTime " +
            "order by b.end desc")
    Page<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByEndDesc(Pageable pageable, Long ownerId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start < :currentTime " +
            "AND b.end < :currentTime " +
            "order by b.end desc")
    Page<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndBeforeOrderByEndDesc(Pageable pageable, Long ownerId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start > :currentTime " +
            "AND b.end > :currentTime " +
            "order by b.end desc")
    Page<Booking> findAllByItem_Owner_IdAndStartAfterAndEndAfterOrderByEndDesc(Pageable pageable,
                                                                               Long ownerId,
                                                                               LocalDateTime currentTime);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStatusNotAndStartAfterOrderByStartAsc(Long itemId,
                                                                                                 Long ownerId,
                                                                                                 BookingStatus status,
                                                                                                 LocalDateTime time);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStatusNotAndStartBeforeOrderByStartDesc(Long itemId,
                                                                                                   Long ownerId,
                                                                                                   BookingStatus status,
                                                                                                   LocalDateTime time);

    List<Booking> findAllByItem_Id(Long itemId);
}
