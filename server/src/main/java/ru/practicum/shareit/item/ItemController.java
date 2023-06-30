package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
                              @RequestBody @Valid ItemUpdateDto itemDto,
                              HttpServletRequest request) {
        log.info("Requested Item create:" +
                        "\nITEM:    {}," +
                        "\nUser ID: {}" +
                        "\nfrom IP: {}",
                itemDto, ownerId, request.getRemoteAddr()
        );
        return itemService.create(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(value = SHARER_USER_ID) long userId,
                                    @RequestBody @Valid CommentDto comment,
                                    @PathVariable(name = "itemId") long itemId,
                                    HttpServletRequest request) {
        log.info("Requested COMMENT create:" +
                        "\nCOMMENT:    {}," +
                        "\nUser ID: {}" +
                        "\nfrom IP: {}",
                comment, userId, request.getRemoteAddr()
        );
        return itemService.createItemComment(comment, userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable long itemId,
                                              @RequestHeader(value = SHARER_USER_ID) long ownerId,
                                              @RequestBody ItemUpdateDto item,
                                              HttpServletRequest request) {
        log.info("Requested ITEM update:" +
                        "\nnew ITEM data:    {}," +
                        "\nUser ID: {}" +
                        "\nItem ID: {}" +
                        "\nfrom IP: {}",
                item, ownerId, itemId, request.getRemoteAddr()
        );
        return new ResponseEntity<>(itemService.updateItem(itemId, item, ownerId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(@RequestHeader(value = SHARER_USER_ID) long ownerId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                                     HttpServletRequest request) {
        log.info("Get all ITEMs request:" +
                        "\nOwner ID: {}" +
                        "\nfrom IP: {}",
                ownerId, request.getRemoteAddr()
        );
        return new ResponseEntity<>(itemService.getItemsOfOwner(ownerId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable long itemId,
                                           @RequestHeader(value = SHARER_USER_ID) long ownerId,
                                           HttpServletRequest request) {
        log.info("Get ITEM request:" +
                        "\nOwner ID: {}" +
                        "\nItem ID: {}" +
                        "\nfrom IP: {}",
                ownerId, itemId, request.getRemoteAddr()
        );
        return new ResponseEntity<>(itemService.getItem(itemId, ownerId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItem(@RequestParam("text") String text,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                                    HttpServletRequest request) {
        log.info("Search ITEMs request:" +
                        "\nSearch text: {}" +
                        "\nfrom IP: {}",
                text, request.getRemoteAddr()
        );
        return new ResponseEntity<>(itemService.searchItems(text, from, size), HttpStatus.OK);
    }
}
