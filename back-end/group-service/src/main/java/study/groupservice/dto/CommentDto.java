package study.groupservice.dto;

import lombok.Data;
import study.groupservice.domain.Comment;

@Data
public class CommentDto {

    private Long commentId;
    private String content;
    private String memberId;
    private String memberName;

    public static CommentDto from(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.commentId = comment.getId();
        commentDto.content = comment.getContent();
        commentDto.memberId = comment.getMemberId();
        return commentDto;
    }
}
