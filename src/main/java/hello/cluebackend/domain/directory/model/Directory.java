package hello.cluebackend.domain.directory.model;

import hello.cluebackend.application.directory.dto.RequestDirectoryDto;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.application.directory.dto.DirectoryDto;
import hello.cluebackend.domain.document.model.Document;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="directory_id", nullable = false, updatable = false)
    private UUID directoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id",  nullable = false)
    private ClassRoom classRoom;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "directory", cascade = CascadeType.ALL)
    private List<Document> documentList;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void update(RequestDirectoryDto requestDirectoryDto) {
        this.name = requestDirectoryDto.getName();
    }
}
