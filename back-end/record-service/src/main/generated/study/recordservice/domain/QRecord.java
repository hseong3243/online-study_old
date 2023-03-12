package study.recordservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecord is a Querydsl query type for Record
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecord extends EntityPathBase<Record> {

    private static final long serialVersionUID = -1444409588L;

    public static final QRecord record = new QRecord("record");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> endTime = createNumber("endTime", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memberId = createString("memberId");

    public final NumberPath<Long> startTime = createNumber("startTime", Long.class);

    public final EnumPath<StudyCategory> studyCategory = createEnum("studyCategory", StudyCategory.class);

    public final NumberPath<Long> studyId = createNumber("studyId", Long.class);

    public final EnumPath<StudyStatus> studyStatus = createEnum("studyStatus", StudyStatus.class);

    public QRecord(String variable) {
        super(Record.class, forVariable(variable));
    }

    public QRecord(Path<? extends Record> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecord(PathMetadata metadata) {
        super(Record.class, metadata);
    }

}

