package hello.cluebackend.domain.document.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDocument is a Querydsl query type for Document
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDocument extends EntityPathBase<Document> {

    private static final long serialVersionUID = -687513363L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDocument document = new QDocument("document");

    public final hello.cluebackend.domain.classroom.domain.QClassRoom classRoom;

    public final StringPath contentType = createString("contentType");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final hello.cluebackend.domain.directory.domain.QDirectory directory;

    public final ComparablePath<java.util.UUID> documentId = createComparable("documentId", java.util.UUID.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final StringPath title = createString("title");

    public final EnumPath<hello.cluebackend.domain.assignment.domain.FileType> type = createEnum("type", hello.cluebackend.domain.assignment.domain.FileType.class);

    public final StringPath value = createString("value");

    public QDocument(String variable) {
        this(Document.class, forVariable(variable), INITS);
    }

    public QDocument(Path<? extends Document> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDocument(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDocument(PathMetadata metadata, PathInits inits) {
        this(Document.class, metadata, inits);
    }

    public QDocument(Class<? extends Document> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.classRoom = inits.isInitialized("classRoom") ? new hello.cluebackend.domain.classroom.domain.QClassRoom(forProperty("classRoom")) : null;
        this.directory = inits.isInitialized("directory") ? new hello.cluebackend.domain.directory.domain.QDirectory(forProperty("directory"), inits.get("directory")) : null;
    }

}

