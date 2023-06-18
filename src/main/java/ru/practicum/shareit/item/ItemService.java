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
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem(ItemUpdateDto itemUpdateDto, Long ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new ElementNotFoundException("User with id " + ownerId + " is not found"));
        itemUpdateDto.setOwner(user);
        Item item = new Item();
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.updateItemWithDto(item, itemUpdateDto)));
    }

    public ItemDto updateItem(Long itemId, ItemUpdateDto itemUpdateDto, Long ownerId) {
        Item item = itemRepository.save(ItemMapper.updateItemWithDto(
                checkItemAndUserExistAndReturn(ownerId, itemId), itemUpdateDto));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new BadRequestException("User with id " + ownerId + " doest not own item " + itemId);
        }
        return ItemMapper.toItemDto(item);
    }

    public ItemDto getItem(Long itemId, Long ownerId) {
        return ItemMapper.toItemDto(checkItemAndUserExistAndReturn(ownerId, itemId));
    }

    public List<ItemDto> getItemsOfOwner(Long ownerId) {
        return itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId).stream().map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String searchStr) {
        if (searchStr.isEmpty()) {
            return new ArrayList<>();
        }

        return itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableTrue(searchStr).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private Item checkItemAndUserExistAndReturn(Long ownerId, Long itemId) {
        if (!userRepository.existsById(ownerId)) {
            throw new ElementNotFoundException("User with id " + ownerId + " not found");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ElementNotFoundException("Item with id " + itemId + " does not exist"));
        return item;
    }
}
