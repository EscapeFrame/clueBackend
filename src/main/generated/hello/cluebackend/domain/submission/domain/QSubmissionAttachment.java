package hello.cluebackend.domain.submission.domain;

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

    private static final long serialVersionUID = -1140084142L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubmissionAttachment submissionAttachment = new QSubmissionAttachment("submissionAttachment");

    public final StringPath contentType = createString("contentType");

    public final StringPath originalFileName = createString("originalFileName");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final QSubmission submission;

    public final ComparablePath<java.util.UUID> SubmissionAttachmentId = createComparable("SubmissionAttachmentId", java.util.UUID.class);

    public final EnumPath<FileType> type = createEnum("type", FileType.class);

    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public final StringPath value = createString("value");

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
        this.submission = inits.isInitialized("submission") ? new QSubmission(forProperty("submission"), inits.get("submission")) : null;
        this.user = inits.isInitialized("user") ? new hello.cluebackend.domain.user.domain.QUserEntity(forProperty("user")) : null;
    }

}

