package study.groupservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPurpose is a Querydsl query type for Purpose
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurpose extends EntityPathBase<Purpose> {

    private static final long serialVersionUID = 1057270983L;

    public static final QPurpose purpose = new QPurpose("purpose");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<PurposeCategory> purposeCategory = createEnum("purposeCategory", PurposeCategory.class);

    public QPurpose(String variable) {
        super(Purpose.class, forVariable(variable));
    }

    public QPurpose(Path<? extends Purpose> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPurpose(PathMetadata metadata) {
        super(Purpose.class, metadata);
    }

}

