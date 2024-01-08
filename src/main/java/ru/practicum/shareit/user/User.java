package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Data
@RequiredArgsConstructor
public class User {
    @EqualsAndHashCode.Exclude
    private Long id;
    @EqualsAndHashCode.Exclude
    private String name;
    @NotEmpty(message = "Поле email не может быть пустым")
    @Email(message = "email должен соответствовать паттерну name@domen.zone и содержать @")
    private String email;
}
