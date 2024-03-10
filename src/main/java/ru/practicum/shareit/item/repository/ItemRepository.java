package ru.practicum.shareit.item.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerOrderByIdAsc(User user);

    @Query("SELECT it FROM Item it " +
            "WHERE (lower(it.name) LIKE LOWER(concat('%', :text, '%')) " +
            "or lower(it.description) LIKE LOWER(concat('%', :text, '%'))) " +
            "AND available is true " +
            "ORDER BY it.id asc")
    List<Item> findByNameOrDescriptionContainingIgnoreCase(@Param("text") String text);

    Optional<Item> findByIdAndOwner_IdNot(Long itemId, Long ownerId);

    @Query("SELECT it FROM Item it WHERE it.request.id in (:requestsId) ORDER BY it.id ASC")
    List<Item> findAllByRequest_IdIn(List<Long> requestsId);
}
