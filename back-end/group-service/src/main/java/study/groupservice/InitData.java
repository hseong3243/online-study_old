package study.groupservice;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.groupservice.domain.*;
import study.groupservice.params.GroupParams;
import study.groupservice.params.PostParam;
import study.groupservice.params.PurposeParam;
import study.groupservice.repository.CommentRepository;
import study.groupservice.repository.GroupRepository;
import study.groupservice.repository.PostRepository;
import study.groupservice.repository.PurposeRepository;
import study.groupservice.repository.GroupMemberRepository;
import study.groupservice.service.GroupService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

//@Component
@RequiredArgsConstructor
public class InitData {

    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PurposeRepository purposeRepository;
    private final GroupMemberRepository groupMemberRepository;

//    @Transactional
//    @PostConstruct
    public void init() {

        String memberId1 = "ca2c38db-5c51-440b-8ea3-6d02fa624981";
        String memberId2 = "29859139-8578-4050-8ea9-0038ebd22572";
        String memberId3 = "98dcb620-692c-4b11-9aa0-5e19694e3b55";
        String memberId4 = "e84def0d-45cc-4add-be8e-1ca32cef091b";
        String memberId5 = "87a59419-8912-4272-a4c7-7be1a0bcb958";

        List<String> members = List.of(memberId1, memberId2, memberId3, memberId4, memberId5);

        Random random = new Random();

        for(int i=0; i<10; i++) {
            GroupParams groupParams = new GroupParams("그룹이름" + i, (long) random.nextInt(522), null, GroupCategory.UNIV);
            groupService.createGroup(groupParams, members.get(i%5));
        }

//
//        GroupParams groupParams1 = new GroupParams();
//        groupParams1.setName("테스트그룹이다.");
//        groupParams1.setStudyId(1L);
//        Long groupId1 = groupService.createGroup(groupParams1, memberId1);
//
//        GroupParams groupParams2 = new GroupParams();
//        groupParams2.setName("테스트그룹이다.");
//        groupParams2.setStudyId(2L);
//        Long groupId2 = groupService.createGroup(groupParams2, memberId1);
//
//        GroupParams groupParams3 = new GroupParams();
//        groupParams3.setName("테스트그룹이다.");
//        groupParams3.setStudyId(3L);
//        Long groupId3 = groupService.createGroup(groupParams3, memberId1);
//
//        groupParams3.setName("정처기비전공자모임");
//        groupParams3.setStudyId(4L);
//        groupService.createGroup(groupParams3, memberId3);
//
//        groupParams3.setName("토픽초보자만");
//        groupParams3.setStudyId(3L);
//        groupService.createGroup(groupParams3, memberId2);
//
//        Long groupMemberId1 = groupService.joinGroup(groupId1, memberId2);
//        Long groupMemberId2 = groupService.joinGroup(groupId1, memberId3);
//
//        GroupMember groupMember1 = groupMemberRepository.findById(groupMemberId1).get();
//        GroupMember groupMember2 = groupMemberRepository.findById(groupMemberId2).get();
//
//        groupMember1.attendance();
//        groupMember2.attendance();
//
//        Group group = groupRepository.findById(groupId1).get();
//
//        PostParam postParam1 = getPostParam("테스트용 제목1", "흐핳하핳ㅇ", memberId1, SMALLTALK);
//        Post post1 = Post.create(postParam1, memberId1);
//        PostParam postParam2 = getPostParam("테스트용2", "흐핳하핳ㅇㅇㅇ", memberId1, SMALLTALK);
//        Post post2 = Post.create(postParam2, memberId1);
//        PostParam postParam3 = getPostParam("테스트3", "흐핳하핳ㅇㄴㅇㄹ", memberId2, QUESTION);
//        Post post3 = Post.create(postParam3, memberId2);
//        PostParam postParam4 = getPostParam("테스트용 제목4", "흐핳하핳ㄴㅇㄹㅇ", memberId1, INFO);
//        Post post4 = Post.create(postParam4, memberId1);
//        PostParam postParam5 = getPostParam("테스트용 제목55", "흐핳하ㅇㅇ핳ㅇ", memberId3, INFO);
//        Post post5 = Post.create(postParam5, memberId3);
//        PostParam postParam6 = getPostParam("테스트용 6", "흐핳하ㄴㅇㄹ핳ㅇ", memberId2, SMALLTALK);
//        Post post6 = Post.create(postParam6, memberId2);
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//        postRepository.save(post4);
//        postRepository.save(post5);
//        postRepository.save(post6);
//
//        post1.setGroup(group);
//        post2.setGroup(group);
//        post3.setGroup(group);
//        post4.setGroup(group);
//        post5.setGroup(group);
//        post6.setGroup(group);
//
//        CommentDto commentDto = new CommentDto();
//        commentDto.setMemberName(memberId2);
//        commentDto.setContent("댓글 테스트 중입니다.");
//        Comment comment1 = Comment.create(commentDto.getContent(), memberId3);
//        Comment comment2 = Comment.create(commentDto.getContent(), memberId2);
//        Comment comment3 = Comment.create(commentDto.getContent(), memberId3);
//        Comment comment4 = Comment.create(commentDto.getContent(), memberId2);
//        commentRepository.save(comment1);
//        commentRepository.save(comment2);
//        commentRepository.save(comment3);
//        commentRepository.save(comment4);
//        comment1.setPost(post1);
//        comment2.setPost(post1);
//        comment3.setPost(post6);
//        comment4.setPost(post6);

//        PurposeParam purposeParam = new PurposeParam();
//
//        for(int i=6; i<=10; i++) {
//            purposeParam.setContent(i + "시 출석");
//            Purpose purpose1 = Purpose.create(purposeParam);
//            purpose1.setPurposeCategory(PurposeCategory.DAYATTEND);
//            purposeRepository.save(purpose1);
//        }
//        for(int i=1; i<=14; i++) {
//            purposeParam.setContent("하루 " + i + "시간");
//            Purpose purpose1 = Purpose.create(purposeParam);
//            purpose1.setPurposeCategory(PurposeCategory.DAY);
//            purposeRepository.save(purpose1);
//        }
//        for(int i=10; i<=60; i=i+5) {
//            purposeParam.setContent("주간 " + i + "시간");
//            Purpose purpose1 = Purpose.create(purposeParam);
//            purpose1.setPurposeCategory(PurposeCategory.WEEK);
//            purposeRepository.save(purpose1);
//        }
//
//        purposeParam.setContent("매일 출석");
//        Purpose purpose1= Purpose.create(purposeParam);
//        purpose1.setPurposeCategory(PurposeCategory.WEEKATTEND);
//        purposeRepository.save(purpose1);
//        purposeParam.setContent("평일 출석");
//        Purpose purpose2= Purpose.create(purposeParam);
//        purpose2.setPurposeCategory(PurposeCategory.WEEKATTEND);
//        purposeRepository.save(purpose2);
//        purposeParam.setContent("주말 출석");
//        Purpose purpose3= Purpose.create(purposeParam);
//        purpose3.setPurposeCategory(PurposeCategory.WEEKATTEND);
//        purposeRepository.save(purpose3);

    }

    @NotNull
    private PostParam getPostParam(String title, String content, String memberId, PostCategory category) {
        PostParam postParam = new PostParam();
        postParam.setMemberId(memberId);
        postParam.setTitle(title);
        postParam.setContent(content);
        postParam.setCategory(category);
        return postParam;
    }
}
