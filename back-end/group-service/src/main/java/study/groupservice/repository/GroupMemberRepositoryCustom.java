package study.groupservice.repository;

import study.groupservice.domain.Group;

import java.time.LocalDateTime;
import java.util.Optional;

public interface GroupMemberRepositoryCustom {

    Optional<LocalDateTime> findGroupMemberAttend(String memberId, Long studyId);

    Long findCountGroupMembers(Group group);
}
