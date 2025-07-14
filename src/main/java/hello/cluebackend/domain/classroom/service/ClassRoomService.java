package hello.cluebackend.domain.classroom.service;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDto;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.classroomuser.domain.repository.ClassRoomUserRepository;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassRoomService {

    private final ClassRoomUserRepository classRoomUserRepository;
    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;
    public ClassRoomService(ClassRoomUserRepository classRoomUserRepository, ClassRoomRepository classRoomRepository, UserRepository userRepository) {
        this.classRoomUserRepository = classRoomUserRepository;
        this.classRoomRepository = classRoomRepository;
        this.userRepository = userRepository;
    }

    public List<ClassRoomCardDto> findMyClassRoomById(Long id) {
        List<ClassRoomUser> classRoomUsers = classRoomUserRepository.findByUser_UserId(id);
        return classRoomUsers.stream()
                .filter(cu -> cu.getUser().getRole() == Role.STUDENT)
                .map(ClassRoomUser::getClassRoom)
                .map(ClassRoom::toCardDTO)
                .toList();
    }

    @Transactional
    public void createClassRoom(ClassRoomDto classRoomDTO, Long userId) {
        classRoomDTO.generateCode();
        ClassRoom classRoom = classRoomDTO.toEntity();
        classRoomRepository.save(classRoom);

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));

        ClassRoomUser classRoomUser = ClassRoomUser.builder()
                .classRoom(classRoom)
                .user(user)
                .build();
        classRoomUserRepository.save(classRoomUser);
    }

    public void joinClassRoom(Long userId, String code) {
        ClassRoom findClassRoom = classRoomRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("classroom not found"));
        UserEntity findUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        ClassRoomUser classRoomUser = ClassRoomUser.builder()
                .user(findUser)
                .classRoom(findClassRoom)
                .build();
        classRoomUserRepository.save(classRoomUser);
    }

    public ClassRoomDto findById(Long classRoomId) {
        ClassRoom findClassRoom = classRoomRepository.findById(classRoomId).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
        return findClassRoom.toDTO();
    }

    public void updateClassRoom(Long classId, ClassRoomDto classRoomDTO) {
        ClassRoom findClassRoom =  classRoomRepository.findById(classId).orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));
        findClassRoom.setName(classRoomDTO.getName());
        findClassRoom.setSort(classRoomDTO.getSort());
        findClassRoom.setDescription(classRoomDTO.getDescription());
        findClassRoom.setTarget(classRoomDTO.getTarget());
        findClassRoom.setIsActivation(classRoomDTO.getIsActivation());
        classRoomRepository.save(findClassRoom);
    }
}
