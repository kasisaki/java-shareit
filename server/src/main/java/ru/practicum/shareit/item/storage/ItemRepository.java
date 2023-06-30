package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerIdOrderByIdAsc(Long ownerId, Pageable pageable);

    Boolean existsByIdAndOwnerId(Long itemId, Long ownerId);

    Page<Item> findByDescriptionContainingIgnoreCaseAndAvailableTrue(String search, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);
}