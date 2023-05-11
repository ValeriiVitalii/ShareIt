package ru.practicum.shareit.features.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.features.item.model.Item;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "item_answer")
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ItemAnswer {

    @Id
    @Column(name = "item_answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_request_id")
    ItemRequest itemRequest;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
}
