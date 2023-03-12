package study.groupservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import study.groupservice.domain.Group;
import study.groupservice.domain.QGroup;
import study.groupservice.domain.QGroupMember;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static study.groupservice.domain.QGroup.*;
import static study.groupservice.domain.QGroupMember.*;

public class GroupMemberRepositoryImpl implements GroupMemberRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public GroupMemberRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Optional<LocalDateTime> findGroupMemberAttend(String memberId, Long studyId) {
        return Optional.ofNullable(
                query
                .select(groupMember.attend)
                .from(groupMember).join(groupMember.group, group)
                .where(groupMember.memberId.eq(memberId),
                        group.studyId.eq(studyId))
                .fetchOne());
    }

    @Override
    public Long findCountGroupMembers(Group group) {
        Long count = query
                .select(groupMember.memberId.countDistinct())
                .from(groupMember)
                .where(groupMember.group.eq(group))
                .fetchOne();

        return count;
    }
}
