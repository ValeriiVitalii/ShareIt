package ru.practicum.shareit.features.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.features.booking.BookingStatus;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
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
}
