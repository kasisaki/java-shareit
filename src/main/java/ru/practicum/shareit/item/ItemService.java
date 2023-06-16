package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto createItem(ItemUpdateDto itemUpdateDto, Integer ownerId) {
        if (itemUpdateDto.getAvailable() == null || itemUpdateDto.getName().isEmpty()
                || itemUpdateDto.getDescription() == null
                || itemUpdateDto.getDescription().isEmpty()) {
            throw new BadRequestException("Some required item fields are missing");
        }
        User user = userStorage.getUser(ownerId);
        if (user == null) {
            throw new ElementNotFoundException("User " + ownerId + "not found");
        }
        itemUpdateDto.setOwner(user);
        return ItemMapper.toItemDto(itemStorage.createItem(itemUpdateDto));
    }

    public ItemDto updateItem(Integer itemId, ItemUpdateDto itemDto, Integer ownerId) {

        if (userStorage.getUser(ownerId) == null) {
            throw new ElementNotFoundException("User with id " + ownerId + " not found");
        }
        if (itemStorage.getItem(itemId).getOwner().getId() != ownerId) {
            throw new ElementNotFoundException("User with id " + ownerId + "does not have item with id " + itemId);
        }

        return ItemMapper.toItemDto(itemStorage.updateItem(itemDto, itemId));
    }

    public ItemDto getItem(Integer itemId, Integer ownerId) {
        if (userStorage.getUser(ownerId) == null) {
            throw new ElementNotFoundException("User with id " + ownerId + " not found");
        }
        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    public List<ItemDto> getItemsOfOwner(Integer ownerId) {
        ArrayList<Item> items = new ArrayList<>(itemStorage.getAllItems());
        ArrayList<ItemDto> itemsDto = new ArrayList<>();

        for (Item item : items) {
            if (item.getOwner().getId() == ownerId) {
                itemsDto.add(ItemMapper.toItemDto(item));
            }
        }
        return itemsDto;
    }

    public List<ItemDto> searchItems(String searchStr) {
        if (searchStr.isEmpty()) {
            return new ArrayList<>();
        }

        return itemStorage.getAllItems().stream()
                .filter(item -> item.getDescription().toLowerCase().contains(searchStr.toLowerCase()))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
