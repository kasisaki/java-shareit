package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(value = SHARER_USER_ID) long requestorId,
                                                @RequestBody @Valid RequestDto requestDto) {
        return requestClient.createRequest(requestDto, requestorId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequests(@RequestHeader(value = SHARER_USER_ID) long requestorId) {
        return requestClient.getAllRequestsOfUser(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object>
    getRequests(@RequestHeader(value = SHARER_USER_ID) long requestorId,
                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return requestClient.getRequests(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(value = SHARER_USER_ID) long requestorId,
                                             @PathVariable long requestId) {
        return requestClient.getRequest(requestorId, requestId);
    }

}
