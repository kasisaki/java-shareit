package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.ItemComment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.shareit.CommonData.*;
import static ru.practicum.shareit.item.mapper.CommentMapper.mapCommentRequestDtoToComment;
import static ru.practicum.shareit.item.mapper.CommentMapper.mapCommentToCommentResponseDto;
import static ru.practicum.shareit.utils.DateUtils.now;

@SpringBootTest
public class ItemCommentMapperTests {
    private final ItemComment itemCommentToUpdate = new ItemComment();
    @BeforeEach
    void  setup() throws Exception {
        itemCommentToUpdate.setId(0L);
        itemCommentToUpdate.setText("untouched text");
        itemCommentToUpdate.setItem(itemAvailable);
        itemCommentToUpdate.setAuthor(user1);
        itemCommentToUpdate.setCreated(now().minusHours(5));
    }

    @Test
    public void testMapCommentRequestDtoToComment() throws Exception {
        ItemComment comment = mapCommentRequestDtoToComment(commentDto, itemAvailable, user1);
        assertEquals(comment.getItem(), itemAvailable);
        assertEquals(comment.getText(), commentDto.getText());
    }


    @Test
    public void testMapCommentToCommentResponseDtoNullExpectNull() throws Exception {
        assertNull(mapCommentToCommentResponseDto(null));
    }

    @Test
    public void testMapCommentToCommentResponseDto() throws Exception {
        CommentDto dto  = mapCommentToCommentResponseDto(itemCommentToUpdate);

        assertEquals(dto.getCreated(), itemCommentToUpdate.getCreated());
        assertEquals(dto.getAuthorName(), itemCommentToUpdate.getAuthor().getName());
    }

    @Test
    public void testItemCommentMethods() throws Exception {
        assertEquals(77L, (long) commentDto.getId());
        assertEquals("commentText", commentDto.getText());
    }
}
