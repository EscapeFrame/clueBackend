package hello.cluebackend.domain.user.service;

import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.domain.user.presentation.dto.DefaultRegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        UserEntity userEntity = userRepository.findById(userId).get();
        return userEntity.toUserDTO();
    }
}
