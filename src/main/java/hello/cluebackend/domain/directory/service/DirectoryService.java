package hello.cluebackend.domain.directory.service;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.directory.domain.Directory;
import hello.cluebackend.domain.directory.domain.repository.DirectoryRepository;
import hello.cluebackend.domain.directory.presentation.dto.CreateDirectoryDto;
import org.springframework.stereotype.Service;

@Service
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final ClassRoomRepository classRoomRepository;

    public DirectoryService(DirectoryRepository directoryRepository,  ClassRoomRepository classRoomRepository) {
        this.directoryRepository = directoryRepository;
        this.classRoomRepository = classRoomRepository;
    }

    public void createDirectory(CreateDirectoryDto createDirectoryDto) {
        ClassRoom findClassRoom = classRoomRepository.findById(createDirectoryDto.getClassRoomId()).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
        Directory directory = Directory.builder()
                .name(createDirectoryDto.getName())
                .classRoom(findClassRoom)
                .directoryOrder(createDirectoryDto.getDirectoryOrder())
                .build();
        directoryRepository.save(directory);
    }
}
