package hello.cluebackend.application.classroom.mapper;

import hello.cluebackend.application.classroom.dto.ClassRoomAllInfoDto;
import hello.cluebackend.application.classroom.dto.ClassRoomCardDto;
import hello.cluebackend.application.classroom.dto.ClassRoomDto;
import hello.cluebackend.application.directory.dto.DirectoryAllInfoDto;
import hello.cluebackend.application.document.dto.DocumentAllInfoDto;
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

  public ClassRoomCardDto toCardDto(ClassRoom classRoom) {
    return ClassRoomCardDto.builder()
            .classRoomId(classRoom.getClassRoomId())
            .name(classRoom.getName())
            .sort(classRoom.getSort())
            .target(classRoom.getTarget())
            .studentCount(classRoom.getClassRoomUserList().size())
            .isActivation(classRoom.getIsActivation())
            .build();
  }

  public ClassRoomAllInfoDto toAllInfoDto(ClassRoom classRoom, List<DirectoryAllInfoDto> directoryDtoList, List<String> teacherNames) {
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
