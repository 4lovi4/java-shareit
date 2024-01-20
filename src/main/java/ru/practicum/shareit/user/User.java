package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;
    @EqualsAndHashCode.Exclude
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Поле email не может быть пустым")
    @Email(message = "email должен соответствовать паттерну name@domen.zone и содержать @")
    @Column(name = "email", nullable = false)
    private String email;
}
