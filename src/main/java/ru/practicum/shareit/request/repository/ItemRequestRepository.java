package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT r FROM ItemRequest r " +
    "WHERE r.requester.id <> :userId")
    Page<ItemRequest> findAllByRequester_IdNot(Pageable pageable, Long userId);

    @Query("SELECT r FROM ItemRequest r " +
            "WHERE r.requester.id = :requesterId " +
            "ORDER by r.created DESC")
    List<ItemRequest> findAllByRequester_IdOrderByCreatedDesc(Long requesterId);
}
