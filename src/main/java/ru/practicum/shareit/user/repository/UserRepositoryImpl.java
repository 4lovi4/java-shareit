package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserNotFoundException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    Map<Long, User> users;
    private Long userCounter;

    public UserRepositoryImpl() {
        userCounter = 0L;
        users = new HashMap<Long, User>();
    }

    @Override
    public List<User> findAllUsers() {
        return users.values()
                .stream()
                .sorted(Comparator.comparing(u -> u.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public User findUserById(Long userId) throws UserNotFoundException {
        return users.get(userId);
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(Long userId, User user) {
        return null;
    }

    @Override
    public int deleteUser(Long userId) {
        return 0;
    }
}
