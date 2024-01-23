package ru.practicum.shareit.item.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner(Long userId);

    Item findById(Long userId, Long itemId);

    @Query("select it from Item it " +
            "where lower(it.name) like lower(concat('%', :text, '%')) " +
            "or lower(it.description) like lower(concat('%', :text, '%')) " +
            "order by it.id asc")
    List<Item> findByNameOrDescriptionContainingIgnoreCase(@Param("text") String text);
}
