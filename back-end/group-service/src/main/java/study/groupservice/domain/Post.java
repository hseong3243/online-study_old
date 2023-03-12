package study.groupservice.domain;

import lombok.Getter;
import study.groupservice.params.PostParam;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String memberId;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public void setGroup(Group group) {
        this.group = group;
    }

    public void update(PostParam postParam) {
        this.title = postParam.getTitle();
        this.content = postParam.getContent();
    }

    public static Post create(PostParam postParam, String memberId) {
        Post post = new Post();
        post.title = postParam.getTitle();
        post.content = postParam.getContent();
        post.memberId = memberId;
        post.postCategory = postParam.getCategory();
        post.createdAt = LocalDateTime.now();
        return post;
    }
}
