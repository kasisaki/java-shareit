package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto createItem(Item item, Integer ownerId) {
        User user = userStorage.getUser(ownerId);
        if (user == null) {
            throw new ElementNotFoundException("User " + ownerId + "not found");
        }
        item.setOwner(user);
        itemStorage.createItem(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto updateItem(Integer itemId, ItemUpdateDto itemDto, Integer ownerId) {

        if (userStorage.getUser(ownerId) == null) {
            throw new ElementNotFoundException("User with id " + ownerId + " not found");
        }
        if (itemStorage.getItem(itemId).getOwner().getId() != ownerId) {
            throw new ElementNotFoundException("User with id " + ownerId + "does not have item with id " + itemId);
        }
        itemStorage.updateItem(itemDto, itemId, ownerId);
        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    public ItemDto getItem(Integer itemId, Integer ownerId) {
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
        ArrayList<Item> items = new ArrayList<>(itemStorage.getAllItems());
        ArrayList<ItemDto> itemsDto = new ArrayList<>();

        if (searchStr.isEmpty()) {
            return itemsDto;
        }
        for (Item item : items) {
            if (item.getDescription().toLowerCase().contains(searchStr.toLowerCase())
            && item.getAvailable()) {
                itemsDto.add(ItemMapper.toItemDto(item));
            }
        }
        return itemsDto;
    }
}
