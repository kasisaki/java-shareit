package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Item {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private Long requestId;

    public Boolean isAvailable() {
        return available;
    }
}
