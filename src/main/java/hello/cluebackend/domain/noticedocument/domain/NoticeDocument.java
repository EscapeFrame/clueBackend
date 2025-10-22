package hello.cluebackend.domain.noticedocument.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.cluebackend.domain.assignment.domain.FileType;
import hello.cluebackend.domain.notice.domain.Notice;
import hello.cluebackend.domain.noticedocument.presentation.dto.response.NoticeDocumentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notice_document")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NoticeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="notice_document_id", nullable = false, updatable = false)
    private UUID noticeFileId;

    @Column(nullable = false)
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    // FILE, URL
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    // 실제 파일이면 S3 Key, URL이면 링크
    @Column(nullable = false)
    private String value;

    // 파일일 경우 메타데이터
    private String originalFileName;
    private String contentType;
    private Long size;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    public NoticeDocumentDto toDto() {
        return NoticeDocumentDto.builder()
                .noticeDocumentId(noticeFileId)
                .title(title)
                .build();
    }
}
