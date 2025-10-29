package hello.cluebackend.domain.classroom.service;

import hello.cluebackend.application.classroom.dto.ClassRoomDto;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.domain.classroomuser.model.ClassRoomUser;
import hello.cluebackend.infrastructure.persistence.classroomuser.ClassRoomUserJpaRepository;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassRoomQueryService {
  private final ClassRoomUserJpaRepository classRoomUserJpaRepository;
  private final ClassRoomJpaRepository classRoomJpaRepository;
  private final UserJpaRepository userJpaRepository;
  private final ClassRoomCommandService classRoomCommandService;

  // 교실 생성 (선생)
  @Transactional
  public void createClassRoom(ClassRoomDto classRoomDTO, UUID userId) {
    classRoomDTO.generateCode();
    ClassRoom classRoom = classRoomDTO.toEntity();
    classRoomJpaRepository.save(classRoom);
    UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user not found"));
    ClassRoomUser classRoomUser = ClassRoomUser.create(classRoom, user);
    classRoomUserJpaRepository.save(classRoomUser);
  }

  // 교실 수정 (선생)
  public void updateClassRoom(UUID classId, UUID userId, ClassRoomDto classRoomDTO) {
    classRoomCommandService.validateOwner(userId, classId);
    ClassRoom findClassRoom =  classRoomJpaRepository.findById(classId).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
    findClassRoom.update(classRoomDTO);
    classRoomJpaRepository.save(findClassRoom);
  }

  // 교실 삭제 (선생)
  public void deleteClassRoom(UUID userId, UUID classId) {
    classRoomCommandService.validateOwner(userId, classId);
    ClassRoom classRoom = classRoomCommandService.findByIdOrElseThrow(classId);
    classRoomJpaRepository.delete(classRoom);
  }

  // 교실 참여 (전체)
  public void joinClassRoom(UUID userId, String code) {
    ClassRoom findClassRoom = classRoomJpaRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("classroom not found"));
    UserEntity findUser = userJpaRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
    ClassRoomUser classRoomUser = ClassRoomUser.create(findClassRoom, findUser);
    classRoomUserJpaRepository.save(classRoomUser);
  }
}
