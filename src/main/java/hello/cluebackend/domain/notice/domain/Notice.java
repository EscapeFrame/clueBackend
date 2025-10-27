package hello.cluebackend.domain.notice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.cluebackend.domain.notice.controller.dto.request.ModifyNoticeDto;
import hello.cluebackend.domain.notice.controller.dto.response.NoticeDto;
import hello.cluebackend.domain.notice.controller.dto.response.NoticeInfoDto;
import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class
Notice {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    @Column(name="notice_id", nullable = false, updatable = false)
    private UUID noticeId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NoticeType type;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size=100)
    private List<NoticeDocument> noticeDocuments = new ArrayList<>();

    public NoticeDto toDto() {
        return NoticeDto.builder()
                .noticeId(noticeId)
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .type(type)
                .build();
    }

    public NoticeInfoDto toInfoDto() {
        return NoticeInfoDto.builder()
                .type(type)
                .noticeId(noticeId)
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .build();
    }

    public void modify(ModifyNoticeDto modifyNoticeDto) {
        this.title = modifyNoticeDto.getTitle();
        this.content = modifyNoticeDto.getContent();
        this.type = modifyNoticeDto.getType();
    }
}
