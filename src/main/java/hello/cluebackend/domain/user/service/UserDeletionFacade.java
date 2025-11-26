package hello.cluebackend.domain.user.service;

import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.persistence.agent.AgentJpaRepository;
import hello.cluebackend.infrastructure.persistence.assignment.AssignmentJpaRepository;
import hello.cluebackend.infrastructure.persistence.classroomuser.ClassRoomUserJpaRepository;
import hello.cluebackend.infrastructure.persistence.notice.NoticeJpaRepository;
import hello.cluebackend.infrastructure.persistence.quizroom.QuizRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.subject.SubjectJpaRepository;
import hello.cluebackend.infrastructure.persistence.submission.SubmissionJpaRepository;
import hello.cluebackend.infrastructure.persistence.submissionattachment.SubmissionAttachmentJpaRepository;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDeletionFacade {

    private final UserJpaRepository userJpaRepository;
    private final ClassRoomUserJpaRepository classRoomUserJpaRepository;
    private final SubmissionAttachmentJpaRepository submissionAttachmentJpaRepository;
    private final SubmissionJpaRepository submissionJpaRepository;
    private final AssignmentJpaRepository assignmentJpaRepository;
    private final SubjectJpaRepository subjectJpaRepository;
    private final NoticeJpaRepository noticeJpaRepository;
    private final QuizRoomJpaRepository quizRoomJpaRepository;
    private final AgentJpaRepository agentJpaRepository;

    private final FileService fileService;

    public void deleteUser(UUID userId) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        submissionAttachmentJpaRepository.deleteByUser(user);

        submissionJpaRepository.deleteByUser(user);
        assignmentJpaRepository.deleteByUser(user);
        classRoomUserJpaRepository.deleteByUser(user);

        noticeJpaRepository.deleteByUser(user);
        quizRoomJpaRepository.deleteByHost(user);
        subjectJpaRepository.deleteByTeacher(user);
        agentJpaRepository.deleteByUser(user);

        fileService.deleteFile(user.getValue());

        userJpaRepository.delete(user);
    }
}