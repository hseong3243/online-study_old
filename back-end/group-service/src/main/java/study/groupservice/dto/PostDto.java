package study.groupservice.dto;

import lombok.Data;
import study.groupservice.domain.Post;
import study.groupservice.domain.PostCategory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostDto {

    private Long postId;
    private String title;
    private String content;
    private PostCategory category;
    private long createdAt;
    private List<CommentDto> comments = new ArrayList<>();
    private int commentSize;
    private String memberId;
    private String memberName;

    public static PostDto from(Post post) {
        PostDto postDto = new PostDto();
        postDto.postId = post.getId();
        postDto.title = post.getTitle();
        postDto.content = post.getContent();
        postDto.category = post.getPostCategory();
        postDto.createdAt = post.getCreatedAt().toEpochSecond(ZoneOffset.of("+09:00"));
        postDto.memberId = post.getMemberId();
        return postDto;
    }
}
