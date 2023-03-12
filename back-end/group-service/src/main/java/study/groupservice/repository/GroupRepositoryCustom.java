package study.groupservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.groupservice.domain.Group;
import study.groupservice.domain.GroupCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupRepositoryCustom {

    Optional<Group> findGroupAndPurposes(Long groupId);

    Page<Group> findAllOrderByCreated(String search, GroupCategory groupCategory, Pageable pageable);

    Page<Group> findAllByMemberId(String search, String memberId, Pageable pageable);

    Page<Group> findGroupsOrderByMembers(String search, GroupCategory groupCategory, Pageable pageable);

    Page<Group> findGroupsOrderByAttend(String search, GroupCategory groupCategory, LocalDateTime date, Pageable pageable);
}
