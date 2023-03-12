package study.groupservice.params;

import lombok.Data;
import study.groupservice.domain.PostCategory;

@Data
public class PostParam {

    private String title;
    private String content;
    private String memberId;
    private PostCategory category;
}
