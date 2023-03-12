package study.memberservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.memberservice.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String username);

    @Query(value = "select m from Member m where m.memberId IN :memberIds")
    List<Member> findAllByMemberId(@Param("memberIds") List<String> memberIds);

    Optional<Member> findByMemberId(String memberId);

    @Query(value = "select m from Member m where m.id >= 5117")
    List<Member> findTestMembers();
}
