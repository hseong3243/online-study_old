package study.groupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.groupservice.domain.Group;
import study.groupservice.domain.GroupMember;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>, GroupMemberRepositoryCustom {
    List<GroupMember> findByGroup(Group group);

    Optional<GroupMember> findByGroupAndAndMemberId(Group group, String memberId);

    void deleteByGroupAndMemberId(Group group, String memberId);

    Optional<GroupMember> findByMemberIdAndAttend(String memberId, LocalDateTime attend);

//    Optional<GroupMember> findByStudyIdAndMemberId(Long studyId, String memberId);
}
