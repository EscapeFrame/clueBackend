package hello.cluebackend.domain.classroom.service;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.classroom.controller.dto.ClassRoomAllInfoDto;
import hello.cluebackend.domain.classroom.controller.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.controller.dto.ClassRoomDto;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.classroomuser.domain.repository.ClassRoomUserRepository;
import hello.cluebackend.domain.directory.domain.Directory;
import hello.cluebackend.domain.directory.controller.dto.DirectoryAllInfoDto;
import hello.cluebackend.domain.document.domain.Document;
import hello.cluebackend.domain.document.controller.dto.DocumentAllInfoDto;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassRoomService {
  private final ClassRoomUserRepository classRoomUserRepository;
  private final ClassRoomRepository classRoomRepository;
  private final UserRepository userRepository;

  public List<ClassRoomCardDto> findMyClassRoomById(UUID userId) {
    List<ClassRoomUser> classRoomUsers = classRoomUserRepository.findByUser_UserId(userId);
    return classRoomUsers.stream()
  //                .filter(cu -> cu.getUser().getRole() == Role.STUDENT)
            .map(ClassRoomUser::getClassRoom)
            .map(ClassRoom::toCardDTO)
            .toList();
  }

  @Transactional
  public void createClassRoom(ClassRoomDto classRoomDTO, UUID userId) {
    classRoomDTO.generateCode();
    ClassRoom classRoom = classRoomDTO.toEntity();
    classRoomRepository.save(classRoom);

    UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user not found"));

    ClassRoomUser classRoomUser = ClassRoomUser.builder()
      .classRoom(classRoom)
      .user(user)
      .build();
    classRoomUserRepository.save(classRoomUser);
  }

  public void joinClassRoom(UUID userId, String code) {
    ClassRoom findClassRoom = classRoomRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("classroom not found"));
    UserEntity findUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
    ClassRoomUser classRoomUser = ClassRoomUser.builder()
      .user(findUser)
      .classRoom(findClassRoom)
      .build();
    classRoomUserRepository.save(classRoomUser);
  }

  public ClassRoomDto findById(UUID userId, UUID classRoomId) {
  ClassRoom findClassRoom = classRoomRepository.findByIdWithTeachers(classRoomId);
  return findClassRoom.toDTO();
  }

  public void updateClassRoom(UUID classId, ClassRoomDto classRoomDTO) {
    ClassRoom findClassRoom =  classRoomRepository.findById(classId).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
    findClassRoom.update(classRoomDTO);
    classRoomRepository.save(findClassRoom);
  }

  public ClassRoomAllInfoDto getAllInfo(UUID classId) {
    ClassRoom classRoom = classRoomRepository.findById(classId)
      .orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));

    List<DirectoryAllInfoDto> directoryDtoList = classRoom.getDirectoryList().stream()
            .sorted(Comparator.comparingInt(Directory::getDirectoryOrder))
            .map(directory -> {
              List<DocumentAllInfoDto> documentDtoList = directory.getDocumentList().stream()
                      .sorted(Comparator.comparing(Document::getCreatedAt))
                      .map(doc -> DocumentAllInfoDto.builder()
                              .documentId(doc.getDocumentId())
                              .title(doc.getTitle())
                              .createdAt(doc.getCreatedAt())
                              .build())
                      .collect(Collectors.toList());


              return DirectoryAllInfoDto.builder()
                  .directoryId(directory.getDirectoryId())
                  .directoryName(directory.getName())
                  .directoryOrder(directory.getDirectoryOrder())
                  .documentList(documentDtoList)
                  .build();
      }).collect(Collectors.toList());
    List<UserEntity> findUsers = classRoomUserRepository.findUsersByClassRoomId(classId);

    List<String> teacherNames = findUsers.stream()
            .filter(user -> user.getRole() == Role.TEACHER)
            .map(UserEntity::getUsername)
            .collect(Collectors.toList());

    return ClassRoomAllInfoDto.builder()
            .classRoomId(classRoom.getClassRoomId())
            .classRoomName(classRoom.getName())
            .description(classRoom.getDescription())
            .directoryList(directoryDtoList)
            .teacherNames(teacherNames)
            .code(classRoom.getCode())
            .build();
  }
}
