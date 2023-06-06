package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(ItemDto item) {
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(ItemDto item) {
        return item;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(String id) {
        return new ItemDto();
    }

    @GetMapping("/search?text={text}")
    public ItemDto search(String text) {
        return new ItemDto();
    }
}
