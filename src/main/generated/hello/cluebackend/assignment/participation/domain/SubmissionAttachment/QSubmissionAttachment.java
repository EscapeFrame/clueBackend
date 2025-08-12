package hello.cluebackend.assignment.participation.domain.SubmissionAttachment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubmissionAttachment is a Querydsl query type for SubmissionAttachment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubmissionAttachment extends EntityPathBase<SubmissionAttachment> {

    private static final long serialVersionUID = -418232925L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubmissionAttachment submissionAttachment = new QSubmissionAttachment("submissionAttachment");

    public final hello.cluebackend.assignment.management.domain.QAssignment assignment;

    public final hello.cluebackend.assignment.participation.domain.QSubmission submission;

    public final NumberPath<Long> SubmissionAttachmentId = createNumber("SubmissionAttachmentId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updateDate = createDateTime("updateDate", java.time.LocalDateTime.class);

    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QSubmissionAttachment(String variable) {
        this(SubmissionAttachment.class, forVariable(variable), INITS);
    }

    public QSubmissionAttachment(Path<? extends SubmissionAttachment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubmissionAttachment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubmissionAttachment(PathMetadata metadata, PathInits inits) {
        this(SubmissionAttachment.class, metadata, inits);
    }

    public QSubmissionAttachment(Class<? extends SubmissionAttachment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.assignment = inits.isInitialized("assignment") ? new hello.cluebackend.assignment.management.domain.QAssignment(forProperty("assignment"), inits.get("assignment")) : null;
        this.submission = inits.isInitialized("submission") ? new hello.cluebackend.assignment.participation.domain.QSubmission(forProperty("submission"), inits.get("submission")) : null;
        this.user = inits.isInitialized("user") ? new hello.cluebackend.domain.user.domain.QUserEntity(forProperty("user")) : null;
    }

}

