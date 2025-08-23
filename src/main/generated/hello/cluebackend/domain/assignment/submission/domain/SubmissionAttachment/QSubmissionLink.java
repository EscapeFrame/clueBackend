package hello.cluebackend.domain.assignment.submission.domain.SubmissionAttachment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment.SubmissionLink;


/**
 * QSubmissionLink is a Querydsl query type for SubmissionLink
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubmissionLink extends EntityPathBase<SubmissionLink> {

    private static final long serialVersionUID = -1755446578L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubmissionLink submissionLink = new QSubmissionLink("submissionLink");

    public final QSubmissionAttachment _super;

    // inherited
    public final hello.cluebackend.domain.assignment.assignment.domain.QAssignment assignment;

    // inherited
    public final hello.cluebackend.domain.assignment.submission.domain.QSubmission submission;

    //inherited
    public final NumberPath<Long> SubmissionAttachmentId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate;

    public final StringPath url = createString("url");

    // inherited
    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QSubmissionLink(String variable) {
        this(SubmissionLink.class, forVariable(variable), INITS);
    }

    public QSubmissionLink(Path<? extends SubmissionLink> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubmissionLink(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubmissionLink(PathMetadata metadata, PathInits inits) {
        this(SubmissionLink.class, metadata, inits);
    }

    public QSubmissionLink(Class<? extends SubmissionLink> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QSubmissionAttachment(type, metadata, inits);
        this.assignment = _super.assignment;
        this.submission = _super.submission;
        this.SubmissionAttachmentId = _super.SubmissionAttachmentId;
        this.updateDate = _super.updateDate;
        this.user = _super.user;
    }

}

