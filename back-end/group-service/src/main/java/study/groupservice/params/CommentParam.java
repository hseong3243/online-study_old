package study.groupservice.params;

import lombok.Data;

@Data
public class CommentParam {

    private Long postId;
    private String content;
}
