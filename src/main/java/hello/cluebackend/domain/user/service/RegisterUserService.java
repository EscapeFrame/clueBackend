package hello.cluebackend.domain.user.service;

import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.domain.user.presentation.dto.DefaultRegisterUserDTO;
import hello.cluebackend.domain.user.presentation.dto.RegisterUserDTO;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {
    private final UserRepository userRepository;
    public RegisterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(DefaultRegisterUserDTO userDTO) {
        UserEntity userEntity = new UserEntity(
                userDTO.getStudentId(),
                userDTO.getUsername(),
                userDTO.getStudentId()+userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getRole()
        );
        userRepository.save(userEntity);
    }
}
