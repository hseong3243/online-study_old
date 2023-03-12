package study.groupservice.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import study.groupservice.domain.Group;
import study.groupservice.domain.GroupCategory;
import study.groupservice.domain.QGroup;
import study.groupservice.domain.QGroupMember;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static study.groupservice.domain.QGroup.*;
import static study.groupservice.domain.QGroupMember.*;

public class GroupRepositoryImpl implements GroupRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public GroupRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public Optional<Group> findGroupAndPurposes(Long groupId) {
        query
                .selectFrom(group)
                .join(group.GroupPurposes)
                .where(group.id.eq(groupId))
                .fetch();
        return null;
    }

    @Override
    public Page<Group> findAllOrderByCreated(String search, GroupCategory groupCategory, Pageable pageable) {
        List<Group> result = query
                .select(group)
                .from(group)
                .where(groupCategoryEq(groupCategory),
                        searchNameContains(search))
                .orderBy(group.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(group.countDistinct())
                .from(group)
                .where(groupCategoryEq(groupCategory),
                        searchNameContains(search))
                .fetchOne();

        if(count == null)
            count = 0L;

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<Group> findAllByMemberId(String search, String memberId, Pageable pageable) {
        List<Group> groups = query
                .select(group)
                .from(group).join(group.groupMembers, groupMember)
                .where(groupMember.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(group.countDistinct())
                .from(group).join(group.groupMembers, groupMember)
                .where(groupMember.memberId.eq(memberId))
                .fetchOne();

        if(count == null) {
            count = 0L;
        }

        return new PageImpl<>(groups, pageable, count);

    }

    @Override
    public Page<Group> findGroupsOrderByMembers(String search, GroupCategory groupCategory, Pageable pageable) {
        List<Group> result = query
                .select(group)
                .from(group).join(group.groupMembers, groupMember)
                .where(groupMember.group.eq(group),
                        groupCategoryEq(groupCategory),
                        searchNameContains(search))
                .groupBy(group.id)
                .orderBy(group.id.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(group.countDistinct())
                .from(group)
                .where(groupCategoryEq(groupCategory),
                        searchNameContains(search))
                .fetchOne();

        if(count == null) {
            count = 0L;
        }

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<Group> findGroupsOrderByAttend(String search, GroupCategory groupCategory,
                                               LocalDateTime date, Pageable pageable) {
        List<Group> result = query
                .select(group)
                .from(group).leftJoin(group.groupMembers, groupMember)
                .on(groupMember.group.eq(group),
                        groupMember.attend.after(date))
                .where(groupCategoryEq(groupCategory),
                        searchNameContains(search))
                .groupBy(group.id)
                .orderBy(groupMember.attend.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(group.countDistinct())
                .from(group)
                .where(groupCategoryEq(groupCategory),
                        searchNameContains(search))
                .fetchOne();

        if(count == null) {
            count = 0L;
        }

        return new PageImpl<>(result, pageable, count);
    }

    private BooleanExpression searchNameContains(String search) {
        return search == null ? null : group.name.containsIgnoreCase(search);
    }

    private BooleanExpression groupCategoryEq(GroupCategory groupCategory) {
        return groupCategory == null ? null : group.groupCategory.eq(groupCategory);
    }

    private BooleanExpression groupMemberAttendAfter(LocalDateTime date) {
        return date == null ? null : groupMember.attend.after(date);
    }
}
