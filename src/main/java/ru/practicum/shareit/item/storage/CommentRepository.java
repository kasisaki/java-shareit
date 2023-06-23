package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.ItemComment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<ItemComment, Long> {
    List<ItemComment> findByItemId(Long id);
}