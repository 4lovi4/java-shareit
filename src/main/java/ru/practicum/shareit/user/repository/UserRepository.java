package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository {
    List<User> findAll();

    User findById(Long userId);

    User createUser(User user);

    User updateUser(Long userId, User user);

    int deleteById(Long userId);
}
