package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long userId);

    User addUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}
