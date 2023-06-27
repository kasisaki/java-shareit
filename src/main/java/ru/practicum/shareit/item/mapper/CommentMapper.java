package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemComment;
import ru.practicum.shareit.user.model.User;

import static java.time.LocalDateTime.now;

public class CommentMapper {
    public static ItemComment mapCommentRequestDtoToComment(CommentDto requestDto, Item item, User user) {
        ItemComment comment = new ItemComment();
        comment.setText(requestDto.getText());
        comment.setItem(item);
        if (user.getName() == requestDto.getAuthorName()) {
            comment.setAuthor(user);
        }
        comment.setCreated(now());
        return comment;
    }

    public static CommentDto mapCommentToCommentResponseDto(ItemComment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto responseDto = new CommentDto();
        responseDto.setId(comment.getId());
        responseDto.setText(comment.getText());
        responseDto.setAuthorName(comment.getAuthor().getName());
        responseDto.setCreated(comment.getCreated());
        return responseDto;
    }
}
