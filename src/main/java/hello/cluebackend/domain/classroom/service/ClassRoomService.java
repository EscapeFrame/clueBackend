package hello.cluebackend.domain.classroom.service;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDTO;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.classroomuser.domain.repository.ClassRoomUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassRoomService {

    private final ClassRoomUserRepository classRoomUserRepository;
    private final ClassRoomRepository classRoomRepository;
    public ClassRoomService(ClassRoomUserRepository classRoomUserRepository, ClassRoomRepository classRoomRepository) {
        this.classRoomUserRepository = classRoomUserRepository;
        this.classRoomRepository = classRoomRepository;
    }

    public List<ClassRoomDTO> findMyClassRoomById(Long id) {
        List<ClassRoomUser> userClassRooms = classRoomUserRepository.findByUser_UserId(id);
        return userClassRooms.stream()
                .map(ClassRoomUser::getClassRoom)
                .map(ClassRoom::toDTO)
                .toList();
    }

    public void createClassRoom(ClassRoomDTO classRoomDTO) {
        classRoomDTO.generateCode();
        ClassRoom classRoom = classRoomDTO.toEntity();
        classRoomRepository.save(classRoom);
    }

}
