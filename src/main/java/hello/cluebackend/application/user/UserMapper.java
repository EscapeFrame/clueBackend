package hello.cluebackend.application.user;

import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.model.UserEntity;
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
}
