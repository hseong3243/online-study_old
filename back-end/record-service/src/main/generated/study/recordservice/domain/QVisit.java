package study.recordservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVisit is a Querydsl query type for Visit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVisit extends EntityPathBase<Visit> {

    private static final long serialVersionUID = 927065904L;

    public static final QVisit visit = new QVisit("visit");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> visited = createNumber("visited", Long.class);

    public QVisit(String variable) {
        super(Visit.class, forVariable(variable));
    }

    public QVisit(Path<? extends Visit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVisit(PathMetadata metadata) {
        super(Visit.class, metadata);
    }

}

