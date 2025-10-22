package hello.cluebackend.domain.user.service;

import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.domain.user.controller.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void registerUser(UserDto userDto, int classCode) {
        UserEntity userEntity = new UserEntity(
                classCode,
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getRole()
        );
        userRepository.save(userEntity);
    }

    public UserDto findById(UUID userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을수 없습니다."));
        return userEntity.toUserDTO();
    }
}
