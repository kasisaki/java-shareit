package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                              @Valid @RequestBody Item item) {
        return itemService.createItem(item, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable int itemId,
                              @RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                              @Valid @RequestBody ItemUpdateDto item) {
        return new ResponseEntity<>(itemService.updateItem(itemId, item, ownerId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        return new ResponseEntity<>(itemService.getItemsOfOwner(ownerId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable int itemId,
                           @RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        return new ResponseEntity<>(itemService.getItem(itemId, ownerId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItem(@RequestParam("text") String text) {
        return new ResponseEntity<>(itemService.searchItems(text), HttpStatus.OK);
    }
}
