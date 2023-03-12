package study.groupservice.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public static Comment create(String content, String memberId) {
        Comment comment = new Comment();
        comment.content = content;
        comment.memberId = memberId;
        return comment;
    }
}
