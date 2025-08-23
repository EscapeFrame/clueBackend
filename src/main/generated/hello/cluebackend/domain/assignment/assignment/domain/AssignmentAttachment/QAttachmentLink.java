package hello.cluebackend.domain.assignment.assignment.domain.AssignmentAttachment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment.AttachmentLink;


/**
 * QAttachmentLink is a Querydsl query type for AttachmentLink
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttachmentLink extends EntityPathBase<AttachmentLink> {

    private static final long serialVersionUID = 217970998L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttachmentLink attachmentLink = new QAttachmentLink("attachmentLink");

    public final QAssignmentAttachment _super;

    // inherited
    public final hello.cluebackend.domain.assignment.assignment.domain.QAssignment assignment;

    //inherited
    public final NumberPath<Long> assignmentAttachmentId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate;

    public final StringPath url = createString("url");

    // inherited
    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QAttachmentLink(String variable) {
        this(AttachmentLink.class, forVariable(variable), INITS);
    }

    public QAttachmentLink(Path<? extends AttachmentLink> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttachmentLink(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttachmentLink(PathMetadata metadata, PathInits inits) {
        this(AttachmentLink.class, metadata, inits);
    }

    public QAttachmentLink(Class<? extends AttachmentLink> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QAssignmentAttachment(type, metadata, inits);
        this.assignment = _super.assignment;
        this.assignmentAttachmentId = _super.assignmentAttachmentId;
        this.updateDate = _super.updateDate;
        this.user = _super.user;
    }

}

