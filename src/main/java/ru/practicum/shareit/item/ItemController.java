package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = SHARER_USER_ID) long ownerId,
                              @RequestBody @Valid ItemUpdateDto itemDto) {
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable long itemId,
                                              @RequestHeader(value = SHARER_USER_ID) long ownerId,
                                              @RequestBody ItemUpdateDto item) {
        return new ResponseEntity<>(itemService.updateItem(itemId, item, ownerId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(@RequestHeader(value = SHARER_USER_ID) long ownerId) {
        return new ResponseEntity<>(itemService.getItemsOfOwner(ownerId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable long itemId,
                                           @RequestHeader(value = SHARER_USER_ID) long ownerId) {
        return new ResponseEntity<>(itemService.getItem(itemId, ownerId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItem(@RequestParam("text") String text) {
        return new ResponseEntity<>(itemService.searchItems(text), HttpStatus.OK);
    }
}
