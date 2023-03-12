package study.groupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.groupservice.domain.Group;
import study.groupservice.domain.GroupPurpose;
import study.groupservice.domain.Purpose;

import java.util.Optional;

public interface GroupPurposeRepository extends JpaRepository<GroupPurpose, Long> {

    Optional<GroupPurpose> findByGroupAndPurpose(Group group, Purpose purpose);
}
