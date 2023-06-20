package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class ItemComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "item", nullable = false)
    private Item item;


    @ManyToOne
    @JoinColumn(name = "commentAuthor")
    private User commentAuthor;

    @Column(name = "time_created")
    private LocalDateTime created;
}
