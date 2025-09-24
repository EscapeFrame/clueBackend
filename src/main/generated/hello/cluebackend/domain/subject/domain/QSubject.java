package hello.cluebackend.domain.subject.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubject is a Querydsl query type for Subject
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubject extends EntityPathBase<Subject> {

    private static final long serialVersionUID = 1368610367L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubject subject = new QSubject("subject");

    public final NumberPath<Integer> grade = createNumber("grade", Integer.class);

    public final EnumPath<SubjectCategory> subjectCategory = createEnum("subjectCategory", SubjectCategory.class);

    public final NumberPath<Long> subjectId = createNumber("subjectId", Long.class);

    public final StringPath subjectName = createString("subjectName");

    public final EnumPath<SubjectType> subjectType = createEnum("subjectType", SubjectType.class);

    public final hello.cluebackend.domain.user.domain.QUserEntity teacher;

    public final NumberPath<Integer> weeklyHours = createNumber("weeklyHours", Integer.class);

    public QSubject(String variable) {
        this(Subject.class, forVariable(variable), INITS);
    }

    public QSubject(Path<? extends Subject> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubject(PathMetadata metadata, PathInits inits) {
        this(Subject.class, metadata, inits);
    }

    public QSubject(Class<? extends Subject> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.teacher = inits.isInitialized("teacher") ? new hello.cluebackend.domain.user.domain.QUserEntity(forProperty("teacher")) : null;
    }

}

