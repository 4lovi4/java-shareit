package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "поле description не должно быть пустым")
    private String description;
    private Long requester;
    private LocalDateTime created;
    private List<ItemResponse> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestDto that = (ItemRequestDto) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(requester, that.requester) && Objects.equals(created, that.created) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requester, created, items);
    }
}
