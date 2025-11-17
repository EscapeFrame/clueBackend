package hello.cluebackend.application.user;

import hello.cluebackend.application.user.dto.UserImage;
import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.model.UserEntity;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public List<String> toTeacherNames(List<UserEntity> users) {
        return users.stream()
                .filter(user -> user.getRole() == Role.TEACHER)
                .map(UserEntity::getUsername)
                .collect(Collectors.toList());
    }

    public UserImage toUserImage(UserEntity user, Resource resource) {
        return UserImage.builder()
                .contentType(user.getContentType())
                .resource(resource)
                .build();
    }
}
