package hello.cluebackend.domain.classroom.service;

import hello.cluebackend.application.classroom.mapper.ClassRoomMapper;
import hello.cluebackend.application.user.UserMapper;
import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.application.classroom.dto.ClassRoomAllInfoDto;
import hello.cluebackend.application.classroom.dto.ClassRoomCardDto;
import hello.cluebackend.application.classroom.dto.ClassRoomDto;
import hello.cluebackend.domain.classroomuser.model.ClassRoomUser;
import hello.cluebackend.infrastructure.persistence.classroomuser.ClassRoomUserJpaRepository;
import hello.cluebackend.domain.directory.model.Directory;
import hello.cluebackend.application.directory.dto.DirectoryAllInfoDto;
import hello.cluebackend.domain.document.model.Document;
import hello.cluebackend.application.document.dto.DocumentAllInfoDto;
import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassRoomCommandService {
  private final ClassRoomUserJpaRepository classRoomUserJpaRepository;
  private final ClassRoomJpaRepository classRoomJpaRepository;
  private final ClassRoomMapper classRoomMapper;
  private final UserMapper userMapper;

  // 내가 속한 모든 교실 조회 (전체)
  public List<ClassRoomCardDto> findMyClassRoomById(UUID userId) {
    List<ClassRoomUser> classRoomUsers = classRoomUserJpaRepository.findByUser_UserId(userId);
    return classRoomUsers.stream()
            .map(ClassRoomUser::getClassRoom)
            .map(classRoomMapper::toCardDto)
            .toList();
  }

  // 교실 단일 조회 (전체)
  public ClassRoomDto findById(UUID userId, UUID classId) {
    validateInClassRoom(userId, classId);
    return classRoomJpaRepository.findByIdWithTeachers(classId).toDTO();
  }

  // 교실 정보 조회 (전체)
  public ClassRoomAllInfoDto getAllInfo(UUID classId) {
    ClassRoom classRoom = classRoomJpaRepository.findById(classId)
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
    List<UserEntity> findUsers = classRoomUserJpaRepository.findUsersByClassRoomId(classId);
    List<String> teacherNames = userMapper.toTeacherNames(findUsers);
   return classRoomMapper.toAllInfoDto(classRoom, directoryDtoList, teacherNames);
  }


  public ClassRoom findByIdOrElseThrow(UUID classId){
    return classRoomJpaRepository.findById(classId).orElseThrow(() -> new EntityNotFoundException("해당 교실은 찾을수 없습니다."));
  }

  // 교실에 대한 선생님 권한 확인
  public void validateOwner(UUID userId, UUID classId) {
    ClassRoomUser classRoomUser = (ClassRoomUser) classRoomUserJpaRepository.findByUser_UserIdAndClassRoom_ClassRoomId(userId, classId)
            .orElseThrow(() -> new AccessDeniedException("해당 교실의 멤버가 아닙니다."));

    if (classRoomUser.getUser().getRole() != Role.TEACHER) {
      throw new AccessDeniedException("해당 작업을 수행할 권한(교사)이 없습니다.");
    }
  }

  // 교실에 속해 있는지 권한 확인
  public void validateInClassRoom(UUID userId, UUID classId) {
    ClassRoomUser classRoomUser = (ClassRoomUser) classRoomUserJpaRepository.findByUser_UserIdAndClassRoom_ClassRoomId(userId, classId)
            .orElseThrow(() -> new AccessDeniedException("해당 교실의 멤버가 아닙니다."));
  }
}