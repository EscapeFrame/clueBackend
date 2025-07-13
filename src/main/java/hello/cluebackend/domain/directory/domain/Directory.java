package hello.cluebackend.domain.directory.domain;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.directory.presentation.dto.DirectoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public DirectoryDto  toDto() {
        return DirectoryDto.builder()
                .directoryId(directoryId)
                .classRoom(classRoom)
                .name(name)
                .directoryOrder(directoryOrder)
                .build();
    }
}
