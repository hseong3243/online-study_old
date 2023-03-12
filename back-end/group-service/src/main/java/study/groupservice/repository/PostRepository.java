package study.groupservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.groupservice.domain.Group;
import study.groupservice.domain.Post;
import study.groupservice.domain.PostCategory;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByGroupOrderByCreatedAtDesc(Group group, Pageable pageable);

    Page<Post> findByGroupAndPostCategoryOrderByCreatedAtDesc(Group group, PostCategory category, Pageable pageable);
}
