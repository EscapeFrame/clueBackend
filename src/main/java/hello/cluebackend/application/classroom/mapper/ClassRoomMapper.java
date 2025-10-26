package hello.cluebackend.application.classroom.mapper;

import hello.cluebackend.application.classroom.dto.ClassRoomDto;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.user.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClassRoomMapper {
  public ClassRoomDto toDto(ClassRoom classRoom) {
    List<String> teacherNames = classRoom.getClassRoomUserList().stream()
            .filter(cu -> cu.getUser().getRole() == Role.TEACHER)
            .map(cu -> cu.getUser().getUsername())
            .toList();

    return ClassRoomDto.builder()
            .classRoomId(classRoom.getClassRoomId())
            .name(classRoom.getName())
            .build();
  }
}
