package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemComment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static ItemComment mapCommentRequestDtoToComment(CommentDto requestDto, Item item, User user) {
        ItemComment comment = new ItemComment();
        comment.setText(requestDto.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static CommentDto mapCommentToCommentResponseDto(ItemComment comment) {
        CommentDto responseDto = new CommentDto();
        responseDto.setId(comment.getId());
        responseDto.setText(comment.getText());
        responseDto.setAuthorName(comment.getAuthor().getName());
        responseDto.setCreated(comment.getCreated());
        return responseDto;
    }
}
