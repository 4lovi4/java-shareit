package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @NotEmpty(message = "Поле email не может быть пустым")
    @Email(message = "email должен соответствовать паттерну name@domen.zone и содержать @")
    private String email;
}
