package study.groupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.groupservice.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}