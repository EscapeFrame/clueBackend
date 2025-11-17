package hello.cluebackend.domain.user.service;

import hello.cluebackend.application.user.UserMapper;
import hello.cluebackend.application.user.dto.request.RegisterUserDto;
import hello.cluebackend.application.user.dto.request.UpdateUserDto;
import hello.cluebackend.application.user.dto.response.UserImage;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import hello.cluebackend.application.user.dto.response.UserDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final FileService fileService;
    private final UserMapper userMapper;

    public void registerUser(UserDto userDto, RegisterUserDto registerUserDto, MultipartFile image) throws IOException {
        String storedFileName = fileService.storeFile(image);
        UserEntity userEntity = UserEntity.create(userDto, registerUserDto, storedFileName, image);
        userJpaRepository.save(userEntity);
    }

    public UserDto findByIdToUserDto(UUID userId) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));
        return userEntity.toUserDTO();
    }

    public UserEntity findById(UUID userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));
    }

    public UserImage getMyImage(UUID userId) {
        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));
        Resource resource = fileService.downloadFile(user.getValue());
        return userMapper.toUserImage(user, resource);
    }

    public void updateProfile(UUID userId, UpdateUserDto userDto) {
        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));
        user.updateProfile(userDto);
    }

    public UserDto getUser(UUID userId) {
        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));
        return user.toUserDTO();
    }

    public void updateImage(UUID userId, MultipartFile image) throws IOException {
        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));
        fileService.deleteFile(user.getValue());
        String storedFileName = fileService.storeFile(image);
        user.updateImage(storedFileName, image);
    }
}
