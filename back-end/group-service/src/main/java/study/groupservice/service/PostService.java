package study.groupservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.groupservice.dto.MemberDto;
import study.groupservice.client.MemberServiceClient;
import study.groupservice.domain.Group;
import study.groupservice.domain.Post;
import study.groupservice.domain.PostCategory;
import study.groupservice.dto.CommentDto;
import study.groupservice.dto.PostDto;
import study.groupservice.params.PostParam;
import study.groupservice.repository.GroupMemberRepository;
import study.groupservice.repository.GroupRepository;
import study.groupservice.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberServiceClient memberServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public Page<PostDto> getPosts(Long groupId, int page, int size) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        PageRequest request = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByGroupOrderByCreatedAtDesc(group, request);
        List<String> memberIds = new ArrayList<>();

        Page<PostDto> result = getPostDtos(posts, memberIds);

        memberIdToMemberName(result, memberIds);

        return result;
    }

    public Page<PostDto> getPosts(Long groupId, PostCategory category, int page, int size) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        PageRequest request = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByGroupAndPostCategoryOrderByCreatedAtDesc(group, category, request);
        List<String> memberIds = new ArrayList<>();
        Page<PostDto> result = getPostDtos(posts, memberIds);

        memberIdToMemberName(result, memberIds);

        return result;
    }

    private void memberIdToMemberName(Page<PostDto> postDtos, List<String> memberIds) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        log.info("Before call member-service");
        List<MemberDto> memberDtos = circuitBreaker.run(() -> memberServiceClient.getMembers(memberIds),
                throwable -> new ArrayList<>());
        log.info("After call member-service");

        Map<String, String> idToName
                = memberDtos.stream().collect(Collectors.toMap(MemberDto::getMemberId, MemberDto::getName));

        postDtos.map(post -> {
            post.setMemberName(idToName.get(post.getMemberId()));
            post.getComments().forEach(comment -> {
                comment.setMemberName(idToName.get(comment.getMemberId()));
            });
            return post;
        });
    }

    @NotNull
    private Page<PostDto> getPostDtos(Page<Post> posts, List<String> memberIds) {
        return posts.map(post -> {
            PostDto postDto = PostDto.from(post);
            memberIds.add(post.getMemberId());
            List<CommentDto> commentDtos = new ArrayList<>();
            post.getComments().forEach(comment -> {
                commentDtos.add(CommentDto.from(comment));
                memberIds.add(comment.getMemberId());
            });
            postDto.setComments(commentDtos);
            return postDto;
        });
    }

    @Transactional
    public Long createPost(Long groupId, PostParam postParam, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        groupMemberRepository.findByGroupAndAndMemberId(group, memberId).orElseThrow(() -> {
            throw new RuntimeException("권한이 없습니다.");
        });

        Post post = Post.create(postParam, memberId);
        postRepository.save(post);
        post.setGroup(group);

        return post.getId();
    }

    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 포스트입니다.");
        });

        PostDto postDto = PostDto.from(post);
        post.getComments().forEach(comment -> {
            CommentDto commentDto = CommentDto.from(comment);
            postDto.getComments().add(commentDto);
        });

        return postDto;
    }

    @Transactional
    public Long updatePost(Long postId, PostParam postParam, String memberId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 포스트입니다.");
        });

        if(!post.getMemberId().equals(memberId)) {
            throw new RuntimeException("잘못된 접근입니다.");
        }

        post.update(postParam);

        return post.getId();
    }

    @Transactional
    public void deletePost(Long postId, String memberId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 포스트입니다.");
        });

        if(!post.getMemberId().equals(memberId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        postRepository.delete(post);

    }
}
