package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.ItemRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestInMemoryStorage implements RequestStorage {

    private final Map<Long, ItemRequest> requests = new HashMap<>();

    public ItemRequest create(ItemRequest request) {
        requests.put(request.getId(), request);
        return request;
    }

    public Long remove(Long requestId) {
        requests.remove(requestId);
        return requestId;
    }

    public Long update(ItemRequest request) {
        requests.put(request.getId(), request);
        return request.getId();
    }
}
