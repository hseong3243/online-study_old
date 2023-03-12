package study.groupservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroup is a Querydsl query type for Group
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroup extends EntityPathBase<Group> {

    private static final long serialVersionUID = 882080872L;

    public static final QGroup group = new QGroup("group1");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final EnumPath<GroupCategory> groupCategory = createEnum("groupCategory", GroupCategory.class);

    public final ListPath<GroupMember, QGroupMember> groupMembers = this.<GroupMember, QGroupMember>createList("groupMembers", GroupMember.class, QGroupMember.class, PathInits.DIRECT2);

    public final ListPath<GroupPurpose, QGroupPurpose> GroupPurposes = this.<GroupPurpose, QGroupPurpose>createList("GroupPurposes", GroupPurpose.class, QGroupPurpose.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath intro = createString("intro");

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final StringPath memberId = createString("memberId");

    public final StringPath name = createString("name");

    public final ListPath<Post, QPost> posts = this.<Post, QPost>createList("posts", Post.class, QPost.class, PathInits.DIRECT2);

    public final NumberPath<Long> studyId = createNumber("studyId", Long.class);

    public QGroup(String variable) {
        super(Group.class, forVariable(variable));
    }

    public QGroup(Path<? extends Group> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroup(PathMetadata metadata) {
        super(Group.class, metadata);
    }

}

