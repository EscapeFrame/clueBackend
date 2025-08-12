package hello.cluebackend.domain.directory.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDirectory is a Querydsl query type for Directory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDirectory extends EntityPathBase<Directory> {

    private static final long serialVersionUID = -1569116961L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDirectory directory = new QDirectory("directory");

    public final hello.cluebackend.domain.classroom.domain.QClassRoom classRoom;

    public final NumberPath<Long> directoryId = createNumber("directoryId", Long.class);

    public final NumberPath<Integer> directoryOrder = createNumber("directoryOrder", Integer.class);

    public final ListPath<hello.cluebackend.domain.document.domain.Document, hello.cluebackend.domain.document.domain.QDocument> documentList = this.<hello.cluebackend.domain.document.domain.Document, hello.cluebackend.domain.document.domain.QDocument>createList("documentList", hello.cluebackend.domain.document.domain.Document.class, hello.cluebackend.domain.document.domain.QDocument.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public QDirectory(String variable) {
        this(Directory.class, forVariable(variable), INITS);
    }

    public QDirectory(Path<? extends Directory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDirectory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDirectory(PathMetadata metadata, PathInits inits) {
        this(Directory.class, metadata, inits);
    }

    public QDirectory(Class<? extends Directory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.classRoom = inits.isInitialized("classRoom") ? new hello.cluebackend.domain.classroom.domain.QClassRoom(forProperty("classRoom")) : null;
    }

}

