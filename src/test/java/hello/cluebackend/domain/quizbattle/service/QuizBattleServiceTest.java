package hello.cluebackend.domain.quizbattle.service;

import hello.cluebackend.application.agent.dto.response.AgentResponse;
import hello.cluebackend.application.quizbattle.dto.QuizGenerationRequest;
import hello.cluebackend.application.quizbattle.dto.QuizGenerationResponse;
import hello.cluebackend.domain.quizbattle.model.QuizQuestion;
import hello.cluebackend.domain.quizbattle.model.QuizRoom;
import hello.cluebackend.domain.quizbattle.model.QuizRoomStatus;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.client.quiz.QuizClient;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.quizroom.QuizRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizBattleServiceTest {

    @Mock
    private QuizRoomJpaRepository quizRoomRepository;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private ClassRoomJpaRepository classRoomRepository;

    @Mock
    private QuizRoomRedisService redisService;

    @Mock
    private QuizClient quizClient;

    @InjectMocks
    private QuizBattleService quizBattleService;

    private UUID hostId;
    private UUID documentId;
    private UserEntity hostUser;

    @BeforeEach
    void setUp() {
        hostId = UUID.randomUUID();
        documentId = UUID.randomUUID();
        hostUser = UserEntity.builder()
                .userId(hostId)
                .username("테스트유저")
                .build();
    }

    // 테스트 1: 방 생성 시 documentId를 전달하면 FastAPI로 문제 생성 요청이 호출되어야 함
    @Test
    @DisplayName("방 생성 시 FastAPI로 문제 생성 요청이 호출되어야 한다")
    void createRoom_shouldCallFastAPIToGenerateQuestions() {
        // given
        Integer questionCount = 10;
        Integer timePerQuestion = 30;

        when(userRepository.findById(hostId)).thenReturn(Optional.of(hostUser));
        when(quizRoomRepository.existsByRoomCode(any())).thenReturn(false);
        when(quizRoomRepository.save(any(QuizRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // FastAPI 응답 Mock
        List<QuizQuestion> mockQuestions = createMockQuestions(questionCount);
        QuizGenerationResponse response = QuizGenerationResponse.builder()
                .questions(mockQuestions)
                .totalQuestions(questionCount)
                .status("success")
                .build();
        AgentResponse<QuizGenerationResponse> agentResponse = new AgentResponse<>(true, "success", response, null);

        when(quizClient.generateQuiz(any(QuizGenerationRequest.class))).thenReturn(agentResponse);

        // when
        QuizRoom room = quizBattleService.createRoom(
                hostId, 50, questionCount, timePerQuestion,
                null, documentId
        );

        // then
        verify(quizClient, times(1)).generateQuiz(any(QuizGenerationRequest.class));
        assertThat(room).isNotNull();
        assertThat(room.getStatus()).isEqualTo(QuizRoomStatus.WAITING);
    }

    // 테스트 2: FastAPI 요청에 documentId가 포함되어야 함
    @Test
    @DisplayName("FastAPI 요청에 documentId가 포함되어야 한다")
    void createRoom_shouldIncludeDocumentIdInFastAPIRequest() {
        // given
        Integer questionCount = 5;

        when(userRepository.findById(hostId)).thenReturn(Optional.of(hostUser));
        when(quizRoomRepository.existsByRoomCode(any())).thenReturn(false);
        when(quizRoomRepository.save(any(QuizRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<QuizQuestion> mockQuestions = createMockQuestions(questionCount);
        QuizGenerationResponse response = QuizGenerationResponse.builder()
                .questions(mockQuestions)
                .build();
        AgentResponse<QuizGenerationResponse> agentResponse = new AgentResponse<>(true, "success", response, null);

        when(quizClient.generateQuiz(any(QuizGenerationRequest.class))).thenReturn(agentResponse);

        // when
        quizBattleService.createRoom(hostId, 50, questionCount, 30, null, documentId);

        // then
        ArgumentCaptor<QuizGenerationRequest> requestCaptor = ArgumentCaptor.forClass(QuizGenerationRequest.class);
        verify(quizClient).generateQuiz(requestCaptor.capture());

        QuizGenerationRequest capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getDocumentId()).isEqualTo(documentId);
        assertThat(capturedRequest.getQuestionCount()).isEqualTo(questionCount);
    }

    // 테스트 3: 생성된 문제가 Redis에 저장되어야 함
    @Test
    @DisplayName("방 생성 시 생성된 문제가 Redis에 저장되어야 한다")
    void createRoom_shouldStoreQuestionsInRedis() {
        // given
        Integer questionCount = 3;

        when(userRepository.findById(hostId)).thenReturn(Optional.of(hostUser));
        when(quizRoomRepository.existsByRoomCode(any())).thenReturn(false);
        when(quizRoomRepository.save(any(QuizRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<QuizQuestion> mockQuestions = createMockQuestions(questionCount);
        QuizGenerationResponse response = QuizGenerationResponse.builder()
                .questions(mockQuestions)
                .build();
        AgentResponse<QuizGenerationResponse> agentResponse = new AgentResponse<>(true, "success", response, null);

        when(quizClient.generateQuiz(any(QuizGenerationRequest.class))).thenReturn(agentResponse);

        // when
        QuizRoom room = quizBattleService.createRoom(hostId, 50, questionCount, 30, null, documentId);

        // then
        verify(redisService, times(1)).storeQuestions(eq(room.getRoomCode()), eq(mockQuestions));
    }

    // 테스트 4: 퀴즈 시작 시 이미 저장된 문제를 사용해야 함 (FastAPI 재호출 없음)
    @Test
    @DisplayName("퀴즈 시작 시 이미 저장된 문제를 사용해야 한다 (FastAPI 재호출 없음)")
    void startQuiz_shouldUseAlreadyStoredQuestions() {
        // given
        String roomCode = "ABC123";
        Integer questionCount = 5;

        QuizRoom room = QuizRoom.builder()
                .roomCode(roomCode)
                .status(QuizRoomStatus.WAITING)
                .questionCount(questionCount)
                .build();

        List<QuizQuestion> storedQuestions = createMockQuestions(questionCount);

        when(quizRoomRepository.findByRoomCode(roomCode)).thenReturn(Optional.of(room));
        when(redisService.getAllQuestions(roomCode)).thenReturn(storedQuestions);
        when(quizRoomRepository.save(any(QuizRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        List<QuizQuestion> questions = quizBattleService.startQuiz(roomCode);

        // then
        verify(quizClient, never()).generateQuiz(any()); // FastAPI 호출 없어야 함
        verify(redisService, times(1)).getAllQuestions(roomCode); // Redis에서 가져와야 함
        assertThat(questions).hasSize(questionCount);
    }

    // 테스트 5: documentId 없이 방 생성 시에도 문제 생성 가능해야 함
    @Test
    @DisplayName("documentId 없이 방 생성 시에도 문제 생성 가능해야 한다")
    void createRoom_shouldWorkWithoutDocumentId() {
        // given
        Integer questionCount = 5;

        when(userRepository.findById(hostId)).thenReturn(Optional.of(hostUser));
        when(quizRoomRepository.existsByRoomCode(any())).thenReturn(false);
        when(quizRoomRepository.save(any(QuizRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<QuizQuestion> mockQuestions = createMockQuestions(questionCount);
        QuizGenerationResponse response = QuizGenerationResponse.builder()
                .questions(mockQuestions)
                .build();
        AgentResponse<QuizGenerationResponse> agentResponse = new AgentResponse<>(true, "success", response, null);

        when(quizClient.generateQuiz(any(QuizGenerationRequest.class))).thenReturn(agentResponse);

        // when
        QuizRoom room = quizBattleService.createRoom(hostId, 50, questionCount, 30, null, null); // documentId = null

        // then
        ArgumentCaptor<QuizGenerationRequest> requestCaptor = ArgumentCaptor.forClass(QuizGenerationRequest.class);
        verify(quizClient).generateQuiz(requestCaptor.capture());

        QuizGenerationRequest capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getDocumentId()).isNull();
        assertThat(room).isNotNull();
    }

    private List<QuizQuestion> createMockQuestions(int count) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(i -> QuizQuestion.builder()
                        .questionNumber(i)
                        .questionText("문제 " + i)
                        .options(List.of("선택지1", "선택지2", "선택지3", "선택지4"))
                        .correctAnswer(0)
                        .timeLimit(30)
                        .explanation("설명 " + i)
                        .difficulty("Medium")
                        .build())
                .toList();
    }
}
