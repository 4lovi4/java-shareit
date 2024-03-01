package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @NonNull
    @NotNull(message = "поле start в Booking не должно быть пустым")
    @Column(name = "start_time")
    private LocalDateTime start;
    @NonNull
    @NotNull(message = "поле end в Booking не должно быть пустым")
    @Column(name = "end_time")
    private LocalDateTime end;
    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", item_id=" + item.getId() +
                ", start=" + start +
                ", end=" + end +
                ", booker_id=" + booker.getId() +
                ", status=" + status +
                '}';
    }
}
