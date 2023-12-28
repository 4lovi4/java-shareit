package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @GetMapping
    public List<User> getAllUsers() {
        return null;
    }

    @GetMapping
    public User getUserById(@PathVariable("id") Long userId) {
        return null;
    }

    @PostMapping
    public User addNewUser(@RequestBody User user) {
        return null;
    }

    @PatchMapping
    public User updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
        return null;
    }

    @DeleteMapping
    public void deleteUser(@PathVariable("id") Long userId) {

    }
}
