package study.groupservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.groupservice.dto.CommentDto;
import study.groupservice.params.CommentParam;
import study.groupservice.service.CommentService;

import java.security.Principal;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<Long> createComment(@RequestBody CommentParam commentParam, Principal principal) {
        String memberId = principal.getName();
        Long commentId = commentService.createComment(commentParam, memberId);

        return ResponseEntity.status(OK).body(commentId);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long commentId) {
        CommentDto comment = commentService.getComment(commentId);

        return ResponseEntity.status(OK).body(comment);
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<Long> updateComment(@PathVariable Long commentId,
                                              @RequestBody CommentParam commentParam, Principal principal) {
        String memberId = principal.getName();
        Long updatedCommentId = commentService.updateComment(commentId, commentParam, memberId);

        return ResponseEntity.status(OK).body(updatedCommentId);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                Principal principal) {
        String memberId = principal.getName();
        commentService.deleteComment(commentId, memberId);

        return ResponseEntity.status(OK).body("accept");
    }
}
