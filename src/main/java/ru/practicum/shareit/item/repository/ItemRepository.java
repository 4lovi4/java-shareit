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

    List<Item> findByOwnerAndAvailableTrue(User user);

    @Query("select it from Item it join fetch it.owner where it.id = :itemId and it.owner = :user")
    Optional<Item> findByIdAndOwner(Long itemId, User user);

    @Query("select it from Item it " +
            "where (lower(it.name) like lower(concat('%', :text, '%')) " +
            "or lower(it.description) like lower(concat('%', :text, '%'))) " +
            "and available is true " +
            "order by it.id asc")
    List<Item> findByNameOrDescriptionContainingIgnoreCase(@Param("text") String text);
    Optional<Item> findByIdAndOwner_IdNot(Long itemId, Long ownerId);
}
