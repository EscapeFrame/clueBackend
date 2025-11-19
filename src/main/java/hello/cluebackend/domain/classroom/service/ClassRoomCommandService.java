package hello.cluebackend.domain.classroom.service;

import hello.cluebackend.application.classroom.dto.ClassRoomDto;
import hello.cluebackend.application.classroom.mapper.ClassRoomMapper;
import hello.cluebackend.domain.classroom.exception.AlreadyJoinedClassRoomException;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.user.service.UserService;
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
@Transactional
public class ClassRoomCommandService {
  private final ClassRoomUserJpaRepository classRoomUserJpaRepository;
  private final ClassRoomJpaRepository classRoomJpaRepository;
  private final UserService userService;
  private final ClassRoomQueryService classRoomQueryService;
  private final ClassRoomMapper classRoomMapper;

  // 교실 생성 (선생)
  public void createClassRoom(ClassRoomDto classRoomDto, UUID userId) {
    classRoomDto.generateCode();
    ClassRoom classRoom = classRoomMapper.fromClassRoomDtoToEntity(classRoomDto);
    classRoomJpaRepository.save(classRoom);
    UserEntity user = userService.findById(userId);
    ClassRoomUser classRoomUser = ClassRoomUser.create(classRoom, user);
    classRoomUserJpaRepository.save(classRoomUser);
  }

  // 교실 수정 (선생)
  public void updateClassRoom(UUID classId, UUID userId, ClassRoomDto classRoomDTO) {
    classRoomQueryService.validateOwner(userId, classId);
    ClassRoom findClassRoom =  classRoomJpaRepository.findById(classId).orElseThrow(() -> new EntityNotFoundException("해당 수업이 존재하지 않습니다."));
    findClassRoom.update(classRoomDTO);
    classRoomJpaRepository.save(findClassRoom);
  }

  // 교실 삭제 (선생)
  public void deleteClassRoom(UUID userId, UUID classId) {
    classRoomQueryService.validateOwner(userId, classId);
    ClassRoom classRoom = classRoomQueryService.findByIdOrElseThrow(classId);
    classRoomJpaRepository.delete(classRoom);
  }

  // 교실 참여 (전체)
  public void joinClassRoom(UUID userId, String code) {
    ClassRoom findClassRoom = classRoomJpaRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("classroom not found"));
    UserEntity findUser = userService.findById(userId);

    if (classRoomUserJpaRepository.existsByClassRoomAndUser(findClassRoom, findUser)) {
      throw new AlreadyJoinedClassRoomException("이미 참여한 교실입니다.");
    }

    ClassRoomUser classRoomUser = ClassRoomUser.create(findClassRoom, findUser);
    classRoomUserJpaRepository.save(classRoomUser);
  }
}
