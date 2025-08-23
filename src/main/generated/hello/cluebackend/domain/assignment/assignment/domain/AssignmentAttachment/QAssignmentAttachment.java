package hello.cluebackend.domain.assignment.assignment.domain.AssignmentAttachment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment.AssignmentAttachment;


/**
 * QAssignmentAttachment is a Querydsl query type for AssignmentAttachment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAssignmentAttachment extends EntityPathBase<AssignmentAttachment> {

    private static final long serialVersionUID = 886859401L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAssignmentAttachment assignmentAttachment = new QAssignmentAttachment("assignmentAttachment");

    public final hello.cluebackend.domain.assignment.assignment.domain.QAssignment assignment;

    public final NumberPath<Long> assignmentAttachmentId = createNumber("assignmentAttachmentId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updateDate = createDateTime("updateDate", java.time.LocalDateTime.class);

    public final hello.cluebackend.domain.user.domain.QUserEntity user;

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
        this.assignment = inits.isInitialized("assignment") ? new hello.cluebackend.domain.assignment.assignment.domain.QAssignment(forProperty("assignment"), inits.get("assignment")) : null;
        this.user = inits.isInitialized("user") ? new hello.cluebackend.domain.user.domain.QUserEntity(forProperty("user")) : null;
    }

}

