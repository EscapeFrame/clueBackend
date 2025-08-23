package hello.cluebackend.domain.classroomuser.application;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.classroomuser.domain.repository.ClassRoomUserRepository;
import hello.cluebackend.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomUserService {
  private final ClassRoomUserRepository classRoomUserRepository;

  // 해당 교실에 속한 모든 사용자 조회
  public List<UserEntity> findAllClassroomUser(ClassRoom classRoom){
    List<ClassRoomUser> classRoomUsers = classRoomUserRepository.findAllByClassRoom(classRoom);
    List<UserEntity> users = classRoomUsers.stream()
            .map(cru -> cru.getUser())
            .toList();
    return users;
  }
}
