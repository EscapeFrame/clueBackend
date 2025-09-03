package hello.cluebackend.domain.classroom.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClassRoom is a Querydsl query type for ClassRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClassRoom extends EntityPathBase<ClassRoom> {

    private static final long serialVersionUID = 1743497663L;

    public static final QClassRoom classRoom = new QClassRoom("classRoom");

    public final ComparablePath<java.util.UUID> classRoomId = createComparable("classRoomId", java.util.UUID.class);

    public final ListPath<hello.cluebackend.domain.classroomuser.domain.ClassRoomUser, hello.cluebackend.domain.classroomuser.domain.QClassRoomUser> classRoomUserList = this.<hello.cluebackend.domain.classroomuser.domain.ClassRoomUser, hello.cluebackend.domain.classroomuser.domain.QClassRoomUser>createList("classRoomUserList", hello.cluebackend.domain.classroomuser.domain.ClassRoomUser.class, hello.cluebackend.domain.classroomuser.domain.QClassRoomUser.class, PathInits.DIRECT2);

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final ListPath<hello.cluebackend.domain.directory.domain.Directory, hello.cluebackend.domain.directory.domain.QDirectory> directoryList = this.<hello.cluebackend.domain.directory.domain.Directory, hello.cluebackend.domain.directory.domain.QDirectory>createList("directoryList", hello.cluebackend.domain.directory.domain.Directory.class, hello.cluebackend.domain.directory.domain.QDirectory.class, PathInits.DIRECT2);

    public final ListPath<hello.cluebackend.domain.document.domain.Document, hello.cluebackend.domain.document.domain.QDocument> documentList = this.<hello.cluebackend.domain.document.domain.Document, hello.cluebackend.domain.document.domain.QDocument>createList("documentList", hello.cluebackend.domain.document.domain.Document.class, hello.cluebackend.domain.document.domain.QDocument.class, PathInits.DIRECT2);

    public final BooleanPath isActivation = createBoolean("isActivation");

    public final StringPath name = createString("name");

    public final StringPath sort = createString("sort");

    public final StringPath target = createString("target");

    public QClassRoom(String variable) {
        super(ClassRoom.class, forVariable(variable));
    }

    public QClassRoom(Path<? extends ClassRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClassRoom(PathMetadata metadata) {
        super(ClassRoom.class, metadata);
    }

}

