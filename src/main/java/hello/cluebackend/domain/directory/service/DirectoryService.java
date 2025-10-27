package hello.cluebackend.domain.directory.service;

import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.domain.directory.model.Directory;
import hello.cluebackend.infrastructure.persistence.directory.DirectoryJpaRepository;
import hello.cluebackend.application.directory.dto.RequestDirectoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryJpaRepository directoryJpaRepository;
    private final ClassRoomJpaRepository classRoomJpaRepository;

    public void createDirectory(RequestDirectoryDto requestDirectoryDto) {
        ClassRoom findClassRoom = classRoomJpaRepository.findById(requestDirectoryDto.getClassRoomId()).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
        Directory directory = Directory.builder()
                .name(requestDirectoryDto.getName())
                .classRoom(findClassRoom)
                .directoryOrder(requestDirectoryDto.getDirectoryOrder())
                .build();
        directoryJpaRepository.save(directory);
    }

    public void updateDirectory(RequestDirectoryDto requestDirectoryDto) {
        ClassRoom findClassRoom = classRoomJpaRepository.findById(requestDirectoryDto.getClassRoomId()).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
        Directory directory = directoryJpaRepository.findById(requestDirectoryDto.getDirectoryId()).orElseThrow(() -> new IllegalArgumentException("해당 디렉토리가 존재하지 않습니다."));

        directory.setName(requestDirectoryDto.getName());
        directory.setDirectoryOrder(requestDirectoryDto.getDirectoryOrder());
        directory.setClassRoom(findClassRoom);

        directoryJpaRepository.save(directory);
    }

    public void deleteById(UUID directoryId) {
        try {
            directoryJpaRepository.deleteById(directoryId);
        } catch(Exception e) {
            throw e;
        }
    }
}
