package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "items", schema = "public")
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Поле name не должно быть пустым")
    @Column(name = "name", nullable = false)
    private String name;
    @NotBlank(message = "Поле description не должно быть пустым")
    @Column(name = "description", nullable = false)
    private String description;
    @NotNull(message = "Поле available не должно быть пустым")
    @Column(name = "available", nullable = false)
    private Boolean available;
    @Column(name = "owner_id")
    private Long owner;
    @Column(name = "request_idХЪ")
    private Long request;

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
