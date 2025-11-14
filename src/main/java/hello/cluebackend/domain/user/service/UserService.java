package hello.cluebackend.domain.user.service;

import hello.cluebackend.application.user.dto.RegisterUserDto;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import hello.cluebackend.application.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;

    public void registerUser(UserDto userDto, RegisterUserDto registerUserDto) {
        UserEntity userEntity = UserEntity.create(userDto, registerUserDto);
        userJpaRepository.save(userEntity);
    }

    public UserDto findByIdToUserDto(UUID userId) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을수 없습니다."));
        return userEntity.toUserDTO();
    }

    public UserEntity findById(UUID userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을수 없습니다."));
    }
}
