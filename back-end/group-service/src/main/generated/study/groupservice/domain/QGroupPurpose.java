package study.groupservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupPurpose is a Querydsl query type for GroupPurpose
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupPurpose extends EntityPathBase<GroupPurpose> {

    private static final long serialVersionUID = -1420307562L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupPurpose groupPurpose = new QGroupPurpose("groupPurpose");

    public final QGroup group;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPurpose purpose;

    public QGroupPurpose(String variable) {
        this(GroupPurpose.class, forVariable(variable), INITS);
    }

    public QGroupPurpose(Path<? extends GroupPurpose> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupPurpose(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupPurpose(PathMetadata metadata, PathInits inits) {
        this(GroupPurpose.class, metadata, inits);
    }

    public QGroupPurpose(Class<? extends GroupPurpose> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QGroup(forProperty("group")) : null;
        this.purpose = inits.isInitialized("purpose") ? new QPurpose(forProperty("purpose")) : null;
    }

}

