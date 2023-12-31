package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    Map<Long, User> users;
    private Long userCounter;

    public UserRepositoryImpl() {
        userCounter = 0L;
        users = new HashMap<>();
    }

    @Override
    public List<User> findAllUsers() {
        return users.values()
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public User findUserById(Long userId) throws UserNotFoundException {
        User user = users.get(userId);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    @Override
    public User createUser(User user) throws UserDuplicateException {
        if (users.values().contains(user)) {
            throw new UserDuplicateException(user);
        }
        Long userId = updateUserCounter();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) throws UserNotFoundException{
        User userStored = users.get(userId);
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(userId);
        }
        String newEmail = user.getEmail();
        userStored.setEmail(newEmail);
        String newName = user.getName();
        if (!Objects.isNull(newName)) {
            userStored.setName(newName);
        }
        return userStored;
    }

    @Override
    public int deleteUser(Long userId) {
        int userDeleted = users.containsKey(userId) ? 1 : 0;
        users.remove(userId);
        return userDeleted;
    }

    private Long updateUserCounter() {
        return userCounter++;
    }
}
