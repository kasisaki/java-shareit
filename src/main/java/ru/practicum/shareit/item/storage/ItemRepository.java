package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerIdOrderByIdAsc(Long ownerId);

    Boolean existsByIdAndOwnerId(Long itemId, Long ownerId);

    List<Item> findByDescriptionContainingIgnoreCaseAndAvailableTrue(String search);
}