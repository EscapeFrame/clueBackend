package hello.cluebackend.domain.linksave;

import hello.cluebackend.application.linksave.dto.response.LinkResponse;
import hello.cluebackend.domain.linksave.model.AuthorizationType;
import hello.cluebackend.domain.linksave.model.SubjectType;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import hello.cluebackend.infrastructure.client.linksave.LinkSaveClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkSaveService {

    private final LinkSaveClient linkSaveClient;
    private final UserService userService;

    public List<LinkResponse> getAll(UUID userId, SubjectType subjectType, AuthorizationType authorizationType, int size, int offset) {
        UserEntity user = userService.findById(userId);
        return linkSaveClient.getAll(userId, user.getGrade(), user.getClassNo(), subjectType, authorizationType, size, offset);
    }
}
