package hello.cluebackend.domain.notice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotice is a Querydsl query type for Notice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotice extends EntityPathBase<Notice> {

    private static final long serialVersionUID = 1885373671L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotice notice = new QNotice("notice");

    public final hello.cluebackend.domain.classroom.domain.QClassRoom classRoom;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final ListPath<hello.cluebackend.domain.noticedocument.domain.NoticeDocument, hello.cluebackend.domain.noticedocument.domain.QNoticeDocument> documents = this.<hello.cluebackend.domain.noticedocument.domain.NoticeDocument, hello.cluebackend.domain.noticedocument.domain.QNoticeDocument>createList("documents", hello.cluebackend.domain.noticedocument.domain.NoticeDocument.class, hello.cluebackend.domain.noticedocument.domain.QNoticeDocument.class, PathInits.DIRECT2);

    public final ComparablePath<java.util.UUID> noticeId = createComparable("noticeId", java.util.UUID.class);

    public final StringPath title = createString("title");

    public final hello.cluebackend.domain.user.domain.QUserEntity user;

    public QNotice(String variable) {
        this(Notice.class, forVariable(variable), INITS);
    }

    public QNotice(Path<? extends Notice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotice(PathMetadata metadata, PathInits inits) {
        this(Notice.class, metadata, inits);
    }

    public QNotice(Class<? extends Notice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.classRoom = inits.isInitialized("classRoom") ? new hello.cluebackend.domain.classroom.domain.QClassRoom(forProperty("classRoom")) : null;
        this.user = inits.isInitialized("user") ? new hello.cluebackend.domain.user.domain.QUserEntity(forProperty("user")) : null;
    }

}

