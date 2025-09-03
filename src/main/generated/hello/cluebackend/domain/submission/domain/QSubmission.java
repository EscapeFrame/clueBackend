package hello.cluebackend.domain.submission.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubmission is a Querydsl query type for Submission
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubmission extends EntityPathBase<Submission> {

    private static final long serialVersionUID = 1469690831L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubmission submission = new QSubmission("submission");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final hello.cluebackend.domain.assignment.domain.QAssignment assignment;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final BooleanPath isSubmitted = createBoolean("isSubmitted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModified = _super.lastModified;

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final ComparablePath<java.util.UUID> submissionId = createComparable("submissionId", java.util.UUID.class);

    public final DateTimePath<java.time.LocalDateTime> submittedAt = createDateTime("submittedAt", java.time.LocalDateTime.class);

    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QSubmission(String variable) {
        this(Submission.class, forVariable(variable), INITS);
    }

    public QSubmission(Path<? extends Submission> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubmission(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubmission(PathMetadata metadata, PathInits inits) {
        this(Submission.class, metadata, inits);
    }

    public QSubmission(Class<? extends Submission> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.assignment = inits.isInitialized("assignment") ? new hello.cluebackend.domain.assignment.domain.QAssignment(forProperty("assignment"), inits.get("assignment")) : null;
        this.user = inits.isInitialized("user") ? new hello.cluebackend.domain.user.domain.QUserEntity(forProperty("user")) : null;
    }

}

