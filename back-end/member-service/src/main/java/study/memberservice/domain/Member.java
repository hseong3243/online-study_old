package study.memberservice.domain;

import lombok.Getter;
import study.memberservice.dto.MemberDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberId;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    private StudyCategory studyCategory;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_info_id")
    private MemberInfo memberInfo;

    public void changeName(String name) {
        this.name = name;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeCategory(StudyCategory studyCategory) {
        this.studyCategory = studyCategory;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public static Member create(MemberDto memberDto) {
        Member member = new Member();
        member.email = memberDto.getEmail();
        member.password = memberDto.getPassword();
        member.name = memberDto.getName();
        member.studyCategory = memberDto.getStudyCategory();
        member.memberId = UUID.randomUUID().toString();
        return member;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }
}
