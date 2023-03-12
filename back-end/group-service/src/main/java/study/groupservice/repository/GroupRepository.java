package study.groupservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.groupservice.domain.Group;
import study.groupservice.domain.GroupCategory;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {
    Page<Group> findAllByGroupCategory(GroupCategory groupCategory, PageRequest request);

}
