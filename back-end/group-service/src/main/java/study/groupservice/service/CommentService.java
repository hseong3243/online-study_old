package study.groupservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.groupservice.dto.MemberDto;
import study.groupservice.client.MemberServiceClient;
import study.groupservice.domain.Comment;
import study.groupservice.domain.Post;
import study.groupservice.dto.CommentDto;
import study.groupservice.params.CommentParam;
import study.groupservice.repository.CommentRepository;
import study.groupservice.repository.PostRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final MemberServiceClient memberServiceClient;

    @Transactional
    public Long createComment(CommentParam commentParam, String memberId) {
        Post post = postRepository.findById(commentParam.getPostId()).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 포스트입니다.");
        });

        Comment comment = Comment.create(commentParam.getContent(), memberId);
        commentRepository.save(comment);
        comment.setPost(post);

        return comment.getId();
    }

    @Transactional
    public Long updateComment(Long commentId, CommentParam commentParam, String memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 코멘트입니다.");
        });

        if(!comment.getMemberId().equals(memberId))
            throw new RuntimeException("권한이 없습니다.");

        comment.updateContent(commentParam.getContent());

        return comment.getId();
    }

    @Transactional
    public void deleteComment(Long commentId, String memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 코멘트입니다.");
        });

        if(!comment.getMemberId().equals(memberId))
            throw new RuntimeException("권한이 없습니다.");

        commentRepository.delete(comment);
    }

    public CommentDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 코멘트입니다.");
        });

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        log.info("Before call member-service");
        MemberDto memberDto = circuitBreaker.run(() -> memberServiceClient.getMember(comment.getMemberId()),
                throwable -> new MemberDto());
        log.info("Before call member-service");

        CommentDto commentDto = CommentDto.from(comment);
        commentDto.setMemberName(memberDto.getName());
        return commentDto;
    }
}
