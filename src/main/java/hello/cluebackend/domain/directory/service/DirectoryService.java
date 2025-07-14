package hello.cluebackend.domain.directory.service;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.directory.domain.Directory;
import hello.cluebackend.domain.directory.domain.repository.DirectoryRepository;
import hello.cluebackend.domain.directory.presentation.dto.RequestDirectoryDto;
import org.springframework.stereotype.Service;

@Service
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final ClassRoomRepository classRoomRepository;

    public DirectoryService(DirectoryRepository directoryRepository,  ClassRoomRepository classRoomRepository) {
        this.directoryRepository = directoryRepository;
        this.classRoomRepository = classRoomRepository;
    }

    public void createDirectory(RequestDirectoryDto requestDirectoryDto) {
        ClassRoom findClassRoom = classRoomRepository.findById(requestDirectoryDto.getClassRoomId()).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
        Directory directory = Directory.builder()
                .name(requestDirectoryDto.getName())
                .classRoom(findClassRoom)
                .directoryOrder(requestDirectoryDto.getDirectoryOrder())
                .build();
        directoryRepository.save(directory);
    }

    public void updateDirectory(RequestDirectoryDto requestDirectoryDto) {
        ClassRoom findClassRoom = classRoomRepository.findById(requestDirectoryDto.getClassRoomId()).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
        Directory directory = directoryRepository.findById(requestDirectoryDto.getDirectoryId()).orElseThrow(() -> new IllegalArgumentException("해당 디렉토리가 존재하지 않습니다."));

        directory.setName(requestDirectoryDto.getName());
        directory.setDirectoryOrder(requestDirectoryDto.getDirectoryOrder());
        directory.setClassRoom(findClassRoom);

        directoryRepository.save(directory);
    }

    public void deleteById(Long directoryId) {
        try {
            directoryRepository.deleteById(directoryId);
        } catch(Exception e) {
            throw e;
        }
    }
}
