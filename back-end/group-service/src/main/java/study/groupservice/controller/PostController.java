package study.groupservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.groupservice.domain.PostCategory;
import study.groupservice.dto.PostDto;
import study.groupservice.dto.Result;
import study.groupservice.params.PostParam;
import study.groupservice.service.PostService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/groups/{groupId}/posts")
    public ResponseEntity<Result<List<PostDto>>> getPosts(@PathVariable Long groupId,
                                                          @RequestParam(required = false) PostCategory category,
                                                          @RequestParam int page, @RequestParam int size) {

        Page<PostDto> posts;
        if(category != null)
            posts = postService.getPosts(groupId, category, page, size);
        else
            posts = postService.getPosts(groupId, page, size);


        Result<List<PostDto>> result = new Result<>();
        result.setData(posts.getContent());
        result.setTotalPages(posts.getTotalPages());
        result.setNumber(posts.getNumber());

        return ResponseEntity.status(OK).body(result);
    }

    @PostMapping("/groups/{groupId}/posts")
    public ResponseEntity<Long> createPost(@PathVariable Long groupId,
                                           @RequestBody PostParam postParam,
                                           Principal principal) {
        String memberId = principal.getName();
        Long postId = postService.createPost(groupId, postParam, memberId);

        return ResponseEntity.status(CREATED).body(postId);
    }

    @GetMapping("/groups/{groupId}/posts/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long groupId, @PathVariable Long postId) {
        PostDto postDto = postService.getPost(postId);

        return ResponseEntity.status(OK).body(postDto);
    }

    @PostMapping("/groups/{groupId}/posts/{postId}")
    public ResponseEntity<Long> updatePost(@PathVariable Long groupId, @PathVariable Long postId,
                                           @RequestBody PostParam postParam, Principal principal) {
        String memberId = principal.getName();
        Long updatedPostId = postService.updatePost(postId, postParam, memberId);

        return ResponseEntity.status(OK).body(updatedPostId);
    }

    @DeleteMapping("/groups/{groupId}/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long groupId, @PathVariable Long postId,
                                             Principal principal) {
        String memberId = principal.getName();
        postService.deletePost(postId, memberId);
        return ResponseEntity.status(OK).body("accept");
    }
}
