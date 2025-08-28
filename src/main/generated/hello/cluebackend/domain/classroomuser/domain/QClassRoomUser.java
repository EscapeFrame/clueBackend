package hello.cluebackend.domain.classroomuser.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClassRoomUser is a Querydsl query type for ClassRoomUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClassRoomUser extends EntityPathBase<ClassRoomUser> {

    private static final long serialVersionUID = 338153279L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClassRoomUser classRoomUser = new QClassRoomUser("classRoomUser");

    public final hello.cluebackend.domain.classroom.domain.QClassRoom classRoom;

    public final NumberPath<Long> classRoomUserId = createNumber("classRoomUserId", Long.class);

    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QClassRoomUser(String variable) {
        this(ClassRoomUser.class, forVariable(variable), INITS);
    }

    public QClassRoomUser(Path<? extends ClassRoomUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClassRoomUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClassRoomUser(PathMetadata metadata, PathInits inits) {
        this(ClassRoomUser.class, metadata, inits);
    }

    public QClassRoomUser(Class<? extends ClassRoomUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.classRoom = inits.isInitialized("classRoom") ? new hello.cluebackend.domain.classroom.domain.QClassRoom(forProperty("classRoom")) : null;
        this.user = inits.isInitialized("user") ? new hello.cluebackend.domain.user.domain.QUserEntity(forProperty("user")) : null;
    }

}

