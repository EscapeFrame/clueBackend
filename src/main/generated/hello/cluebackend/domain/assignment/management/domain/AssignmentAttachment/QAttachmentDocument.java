package hello.cluebackend.domain.assignment.management.domain.AssignmentAttachment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttachmentDocument is a Querydsl query type for AttachmentDocument
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttachmentDocument extends EntityPathBase<AttachmentDocument> {

    private static final long serialVersionUID = -358015913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttachmentDocument attachmentDocument = new QAttachmentDocument("attachmentDocument");

    public final QAssignmentAttachment _super;

    // inherited
    public final hello.cluebackend.domain.assignment.management.domain.QAssignment assignment;

    //inherited
    public final NumberPath<Long> assignmentAttachmentId;

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath storedFileName = createString("storedFileName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate;

    // inherited
    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QAttachmentDocument(String variable) {
        this(AttachmentDocument.class, forVariable(variable), INITS);
    }

    public QAttachmentDocument(Path<? extends AttachmentDocument> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttachmentDocument(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttachmentDocument(PathMetadata metadata, PathInits inits) {
        this(AttachmentDocument.class, metadata, inits);
    }

    public QAttachmentDocument(Class<? extends AttachmentDocument> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QAssignmentAttachment(type, metadata, inits);
        this.assignment = _super.assignment;
        this.assignmentAttachmentId = _super.assignmentAttachmentId;
        this.updateDate = _super.updateDate;
        this.user = _super.user;
    }

}

