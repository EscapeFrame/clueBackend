package hello.cluebackend.domain.classroom.service;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDTO;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.classroomuser.domain.repository.ClassRoomUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassRoomService {

    private final ClassRoomUserRepository classRoomUserRepository;

    public ClassRoomService(ClassRoomUserRepository classRoomUserRepository) {
        this.classRoomUserRepository = classRoomUserRepository;
    }

    public List<ClassRoomDTO> findMyClassRoomById(Long id) {
        List<ClassRoomUser> userClassRooms = classRoomUserRepository.findByUserId(id);
        return userClassRooms.stream()
                .map(ClassRoomUser::getClassRoom)
                .map(ClassRoom::toDTO)
                .toList();
    }

}
