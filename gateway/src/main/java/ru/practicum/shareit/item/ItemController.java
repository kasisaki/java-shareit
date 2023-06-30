package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(value = SHARER_USER_ID) long ownerId,
                                             @RequestBody @Valid ItemUpdateDto itemDto,
                                             HttpServletRequest request) {
        log.info("Requested Item create:" +
                        "\nITEM:    {}," +
                        "\nUser ID: {}" +
                        "\nfrom IP: {}",
                itemDto, ownerId, request.getRemoteAddr()
        );
        return itemClient.createItem(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(value = SHARER_USER_ID) long userId,
                                                @RequestBody @Valid CommentDto comment,
                                                @PathVariable(name = "itemId") long itemId,
                                                HttpServletRequest request) {
        log.info("Requested COMMENT create:" +
                        "\nCOMMENT:    {}," +
                        "\nUser ID: {}" +
                        "\nfrom IP: {}",
                comment, userId, request.getRemoteAddr()
        );
        return itemClient.createComment(comment, userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
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
        return itemClient.updateItem(item, itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(value = SHARER_USER_ID) long ownerId,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                              HttpServletRequest request) {
        log.info("Get all ITEMs request:" +
                        "\nOwner ID: {}" +
                        "\nfrom IP: {}",
                ownerId, request.getRemoteAddr()
        );
        return itemClient.getAllItems(ownerId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader(value = SHARER_USER_ID) long ownerId,
                                          HttpServletRequest request) {
        log.info("Get ITEM request:" +
                        "\nOwner ID: {}" +
                        "\nItem ID: {}" +
                        "\nfrom IP: {}",
                ownerId, itemId, request.getRemoteAddr()
        );
        return itemClient.getItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam("text") String text,
                                             @RequestHeader(value = SHARER_USER_ID) long userId,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                             HttpServletRequest request) {
        log.info("Search ITEMs request:" +
                        "\nSearch text: {}" +
                        "\nfrom IP: {}",
                text, request.getRemoteAddr()
        );
        return itemClient.searchItems(userId, text, from, size);
    }
}
