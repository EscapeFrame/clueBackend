package hello.cluebackend.domain.noticedocument.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeFile is a Querydsl query type for NoticeDocument
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeFile extends EntityPathBase<NoticeDocument> {

    private static final long serialVersionUID = -825016321L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoticeFile noticeFile = new QNoticeFile("noticeFile");

    public final StringPath contentType = createString("contentType");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final hello.cluebackend.domain.notice.model.QNotice notice;

    public final NumberPath<Long> noticeFileId = createNumber("noticeFileId", Long.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final StringPath title = createString("title");

    public final EnumPath<hello.cluebackend.domain.assignment.domain.FileType> type = createEnum("type", hello.cluebackend.domain.assignment.domain.FileType.class);

    public final StringPath value = createString("value");

    public QNoticeFile(String variable) {
        this(NoticeDocument.class, forVariable(variable), INITS);
    }

    public QNoticeFile(Path<? extends NoticeDocument> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoticeFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoticeFile(PathMetadata metadata, PathInits inits) {
        this(NoticeDocument.class, metadata, inits);
    }

    public QNoticeFile(Class<? extends NoticeDocument> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notice = inits.isInitialized("notice") ? new hello.cluebackend.domain.notice.model.QNotice(forProperty("notice"), inits.get("notice")) : null;
    }

}

