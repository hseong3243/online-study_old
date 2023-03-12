package study.recordservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGoal is a Querydsl query type for Goal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGoal extends EntityPathBase<Goal> {

    private static final long serialVersionUID = 29463694L;

    public static final QGoal goal = new QGoal("goal");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memberId = createString("memberId");

    public final NumberPath<Long> targetTime = createNumber("targetTime", Long.class);

    public QGoal(String variable) {
        super(Goal.class, forVariable(variable));
    }

    public QGoal(Path<? extends Goal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGoal(PathMetadata metadata) {
        super(Goal.class, metadata);
    }

}

