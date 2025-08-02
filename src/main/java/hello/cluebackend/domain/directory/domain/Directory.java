package hello.cluebackend.domain.directory.domain;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.directory.presentation.dto.DirectoryDto;
import hello.cluebackend.domain.document.domain.Document;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="directory_id", nullable = false)
    private Long directoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id",  nullable = false)
    private ClassRoom classRoom;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int directoryOrder;

    @OneToMany(mappedBy = "directory", cascade = CascadeType.ALL)
    private List<Document> documentList;

    public DirectoryDto  toDto() {
        return DirectoryDto.builder()
                .directoryId(directoryId)
                .classRoom(classRoom)
                .name(name)
                .directoryOrder(directoryOrder)
                .build();
    }
}
