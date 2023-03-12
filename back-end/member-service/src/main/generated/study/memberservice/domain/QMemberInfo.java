package study.memberservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberInfo is a Querydsl query type for MemberInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberInfo extends EntityPathBase<MemberInfo> {

    private static final long serialVersionUID = -116348550L;

    public static final QMemberInfo memberInfo = new QMemberInfo("memberInfo");

    public final DatePath<java.time.LocalDate> dDay = createDate("dDay", java.time.LocalDate.class);

    public final StringPath goal = createString("goal");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath promise = createString("promise");

    public final NumberPath<Long> targetTime = createNumber("targetTime", Long.class);

    public QMemberInfo(String variable) {
        super(MemberInfo.class, forVariable(variable));
    }

    public QMemberInfo(Path<? extends MemberInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberInfo(PathMetadata metadata) {
        super(MemberInfo.class, metadata);
    }

}

