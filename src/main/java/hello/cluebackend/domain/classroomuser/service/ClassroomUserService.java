package hello.cluebackend.domain.classroomuser.service;

import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomQueryService;
import hello.cluebackend.domain.classroomuser.model.ClassRoomUser;
import hello.cluebackend.infrastructure.persistence.classroomuser.ClassRoomUserJpaRepository;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassroomUserService {
  private final ClassRoomUserJpaRepository classRoomUserJpaRepository;
  private final UserService userService;
  private final ClassRoomQueryService classRoomQueryService;

  // 해당 교실에 속한 모든 사용자 조회
  public List<UserEntity> findAllClassroomUser(ClassRoom classRoom){
    List<ClassRoomUser> classRoomUsers = classRoomUserJpaRepository.findAllByClassRoom(classRoom);
    List<UserEntity> users = classRoomUsers.stream()
            .map(cru -> cru.getUser())
            .toList();
    return users;
  }

  // 수업실에 해당 유저가 속하는지 확인하는 로직
  public boolean isUserInClassroom(UUID classRoomId, UUID userId) {
    ClassRoom classRoom = classRoomQueryService.findById(userId, classRoomId).toEntity();
    UserEntity user = userService.findById(userId).toEntity();

    return classRoomUserJpaRepository.existsByClassRoomAndUser(classRoom,user);
  }
}
