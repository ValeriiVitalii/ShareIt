package ru.practicum.shareit.features.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.features.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "items")
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    @NotBlank
    String name;

    @Column
    @NotBlank
    String description;

    @Column
    @NotNull
    Boolean available;

    @ManyToOne
    @NotNull
    User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(description, item.description) &&
                Objects.equals(available, item.available) &&
                Objects.equals(owner, item.owner);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, owner);
    }
}
