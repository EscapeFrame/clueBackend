package hello.cluebackend.domain.assignment.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAssignmentAttachment is a Querydsl query type for AssignmentAttachment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAssignmentAttachment extends EntityPathBase<AssignmentAttachment> {

    private static final long serialVersionUID = 788938900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAssignmentAttachment assignmentAttachment = new QAssignmentAttachment("assignmentAttachment");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QAssignment assignment;

    public final ComparablePath<java.util.UUID> assignmentAttachmentId = createComparable("assignmentAttachmentId", java.util.UUID.class);

    public final StringPath contentType = createString("contentType");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModified = _super.lastModified;

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final StringPath originalFileName = createString("originalFileName");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final EnumPath<FileType> type = createEnum("type", FileType.class);

    public final StringPath value = createString("value");

    public QAssignmentAttachment(String variable) {
        this(AssignmentAttachment.class, forVariable(variable), INITS);
    }

    public QAssignmentAttachment(Path<? extends AssignmentAttachment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAssignmentAttachment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAssignmentAttachment(PathMetadata metadata, PathInits inits) {
        this(AssignmentAttachment.class, metadata, inits);
    }

    public QAssignmentAttachment(Class<? extends AssignmentAttachment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.assignment = inits.isInitialized("assignment") ? new QAssignment(forProperty("assignment"), inits.get("assignment")) : null;
    }

}

