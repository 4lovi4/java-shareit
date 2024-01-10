package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository {
    List<User> findAllUsers();

    User findUserById(Long userId);

    User createUser(User user);

    User updateUser(Long userId, User user);

    int deleteUser(Long userId);
}
