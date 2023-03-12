package study.memberservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.memberservice.domain.MemberInfo;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
}
