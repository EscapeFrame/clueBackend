package hello.cluebackend.domain.classroomuser.application;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.classroomuser.domain.repository.ClassRoomUserRepository;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomUserService {
  private final ClassRoomUserRepository classRoomUserRepository;
  private final UserService userService;
  private final ClassRoomService classRoomService;

  // 해당 교실에 속한 모든 사용자 조회
  public List<UserEntity> findAllClassroomUser(ClassRoom classRoom){
    List<ClassRoomUser> classRoomUsers = classRoomUserRepository.findAllByClassRoom(classRoom);
    List<UserEntity> users = classRoomUsers.stream()
            .map(cru -> cru.getUser())
            .toList();
    return users;
  }

  // 수업실에 해당 유저가 속하는지 확인하는 로직
  public boolean isUserInClassroom(Long classRoomId, Long userId) {
    ClassRoom classRoom = classRoomService.findById(classRoomId).toEntity();
    UserEntity user = userService.findById(userId).toEntity();

    return classRoomUserRepository.existsByClassRoomAndUser(classRoom,user);
  }
}
