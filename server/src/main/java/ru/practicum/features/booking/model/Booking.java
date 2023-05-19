package ru.practicum.features.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.features.booking.BookingStatus;
import ru.practicum.features.item.model.Item;
import ru.practicum.features.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Column(name = "start_booking")
    LocalDateTime start;

    @NotNull
    @Column(name = "end_booking")
    LocalDateTime end;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item itemId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    User booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) &&
                Objects.equals(start, booking.start) &&
                Objects.equals(end, booking.end) &&
                Objects.equals(itemId, booking.itemId) &&
                Objects.equals(booker, booking.booker) &&
                status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, itemId, booker, status);
    }
}
