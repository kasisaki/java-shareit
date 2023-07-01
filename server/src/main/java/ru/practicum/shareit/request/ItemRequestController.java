package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.SHARER_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@RequestHeader(value = SHARER_USER_ID) long requestorId,
                                                    @RequestBody RequestDto requestDto) {
        return new ResponseEntity<>(requestService.create(requestDto, requestorId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RequestDto>> getAllRequests(@RequestHeader(value = SHARER_USER_ID) long requestorId) {
        return new ResponseEntity<>(requestService.getAllRequestsOfUser(requestorId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RequestDto>>
    getRequests(@RequestHeader(value = SHARER_USER_ID) long requestorId,
                @RequestParam(name = "from", defaultValue = "0") int from,
                @RequestParam(name = "size", defaultValue = "10") int size) {
        return new ResponseEntity<>(requestService.getAllRequests(requestorId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestDto> getItem(@RequestHeader(value = SHARER_USER_ID) long requestorId,
                                              @PathVariable long requestId) {
        return new ResponseEntity<>(requestService.getRequest(requestId, requestorId), HttpStatus.OK);
    }

}
