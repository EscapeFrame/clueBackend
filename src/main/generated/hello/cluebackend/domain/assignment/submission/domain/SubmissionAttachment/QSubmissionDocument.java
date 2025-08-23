package hello.cluebackend.domain.assignment.submission.domain.SubmissionAttachment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment.SubmissionDocument;


/**
 * QSubmissionDocument is a Querydsl query type for SubmissionDocument
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubmissionDocument extends EntityPathBase<SubmissionDocument> {

    private static final long serialVersionUID = -868574737L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubmissionDocument submissionDocument = new QSubmissionDocument("submissionDocument");

    public final QSubmissionAttachment _super;

    // inherited
    public final hello.cluebackend.domain.assignment.assignment.domain.QAssignment assignment;

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Integer> fileSize = createNumber("fileSize", Integer.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath storedFileName = createString("storedFileName");

    // inherited
    public final hello.cluebackend.domain.assignment.submission.domain.QSubmission submission;

    //inherited
    public final NumberPath<Long> SubmissionAttachmentId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate;

    // inherited
    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QSubmissionDocument(String variable) {
        this(SubmissionDocument.class, forVariable(variable), INITS);
    }

    public QSubmissionDocument(Path<? extends SubmissionDocument> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubmissionDocument(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubmissionDocument(PathMetadata metadata, PathInits inits) {
        this(SubmissionDocument.class, metadata, inits);
    }

    public QSubmissionDocument(Class<? extends SubmissionDocument> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QSubmissionAttachment(type, metadata, inits);
        this.assignment = _super.assignment;
        this.submission = _super.submission;
        this.SubmissionAttachmentId = _super.SubmissionAttachmentId;
        this.updateDate = _super.updateDate;
        this.user = _super.user;
    }

}

