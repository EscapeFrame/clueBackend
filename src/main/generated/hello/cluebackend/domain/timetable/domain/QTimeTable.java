package hello.cluebackend.domain.timetable.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTimeTable is a Querydsl query type for TimeTable
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTimeTable extends EntityPathBase<TimeTable> {

    private static final long serialVersionUID = 501054271L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTimeTable timeTable = new QTimeTable("timeTable");

    public final StringPath dayOfWeek = createString("dayOfWeek");

    public final StringPath period = createString("period");

    public final EnumPath<com.nimbusds.openid.connect.sdk.SubjectType> subject = createEnum("subject", com.nimbusds.openid.connect.sdk.SubjectType.class);

    public final EnumPath<com.nimbusds.openid.connect.sdk.SubjectType> subjectName = createEnum("subjectName", com.nimbusds.openid.connect.sdk.SubjectType.class);

    public final NumberPath<Long> timetable_id = createNumber("timetable_id", Long.class);

    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QTimeTable(String variable) {
        this(TimeTable.class, forVariable(variable), INITS);
    }

    public QTimeTable(Path<? extends TimeTable> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTimeTable(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTimeTable(PathMetadata metadata, PathInits inits) {
        this(TimeTable.class, metadata, inits);
    }

    public QTimeTable(Class<? extends TimeTable> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new hello.cluebackend.domain.user.domain.QUserEntity(forProperty("user")) : null;
    }

}

