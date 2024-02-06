package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(
                user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User(
                userDto.getEmail());
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        return user;
    }

}
