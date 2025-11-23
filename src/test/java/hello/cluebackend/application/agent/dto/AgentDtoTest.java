package hello.cluebackend.application.agent.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.cluebackend.application.agent.dto.request.*;
import hello.cluebackend.application.agent.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AgentDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Nested
    @DisplayName("Request DTO Tests")
    class RequestDtoTests {

        @Test
        @DisplayName("AgentRequest(PlanningRequest) 필드 검증") // Planning 요청 데이터가 올바르게 매핑되는지 테스트
        void agentRequest_shouldHaveCorrectFields() {
            AgentRequest request = new AgentRequest(
                "Java Programming",
                "Learn OOP concepts",
                List.of("class", "inheritance", "polymorphism"),
                List.of("https://example.com/java")
            );

            assertThat(request.getStudyingName()).isEqualTo("Java Programming");
            assertThat(request.getLearningPurpose()).isEqualTo("Learn OOP concepts");
            assertThat(request.getMainWords()).containsExactly("class", "inheritance", "polymorphism");
            assertThat(request.getLinks()).containsExactly("https://example.com/java");
        }

        @Test
        @DisplayName("WordItem 필드 검증") // 목차 항목 데이터 매핑 테스트
        void wordItem_shouldHaveCorrectFields() {
            WordItem wordItem = new WordItem(1, "Introduction", 1);

            assertThat(wordItem.getPriority()).isEqualTo(1);
            assertThat(wordItem.getIndex()).isEqualTo("Introduction");
            assertThat(wordItem.getIconNumber()).isEqualTo(1);
        }

        @Test
        @DisplayName("FlowFeedbackRequest 필드 검증") // Flow 피드백 요청 데이터 테스트
        void flowFeedbackRequest_shouldContainWordItems() {
            List<WordItem> words = List.of(
                new WordItem(1, "Chapter 1", 1),
                new WordItem(2, "Chapter 2", 2)
            );
            FlowFeedbackRequest request = new FlowFeedbackRequest(words);

            assertThat(request.getWords()).hasSize(2);
            assertThat(request.getWords().get(0).getIndex()).isEqualTo("Chapter 1");
        }

        @Test
        @DisplayName("DocItem 필드 검증") // 문서 항목 데이터 테스트
        void docItem_shouldHaveCorrectFields() {
            DocItem docItem = new DocItem("Introduction", "This is the introduction content.");

            assertThat(docItem.getIndex()).isEqualTo("Introduction");
            assertThat(docItem.getContent()).isEqualTo("This is the introduction content.");
        }

        @Test
        @DisplayName("DocFeedbackRequest 필드 검증") // Doc 피드백 요청 데이터 테스트
        void docFeedbackRequest_shouldContainDocItems() {
            List<DocItem> docs = List.of(
                new DocItem("Section 1", "Content 1"),
                new DocItem("Section 2", "Content 2")
            );
            DocFeedbackRequest request = new DocFeedbackRequest(docs);

            assertThat(request.getDocs()).hasSize(2);
            assertThat(request.getDocs().get(0).getContent()).isEqualTo("Content 1");
        }

        @Test
        @DisplayName("ChatMessage 필드 검증") // 채팅 메시지 데이터 테스트
        void chatMessage_shouldHaveCorrectFields() {
            ChatMessage message = new ChatMessage("user", "Hello, how are you?");

            assertThat(message.getRole()).isEqualTo("user");
            assertThat(message.getContent()).isEqualTo("Hello, how are you?");
        }

        @Test
        @DisplayName("ChatRequest 필드 검증") // 채팅 요청 데이터 테스트
        void chatRequest_shouldHaveMessageAndHistory() {
            List<ChatMessage> history = List.of(
                new ChatMessage("user", "Hi"),
                new ChatMessage("assistant", "Hello!")
            );
            ChatRequest request = new ChatRequest("How does this work?", history);

            assertThat(request.getMessage()).isEqualTo("How does this work?");
            assertThat(request.getHistory()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Response DTO Tests")
    class ResponseDtoTests {

        @Test
        @DisplayName("AgentResponse Generic Wrapper 필드 검증") // Generic 응답 래퍼가 success, message, data, error 필드를 가지는지 테스트
        void agentResponse_shouldHaveAllFields() {
            AgentData data = new AgentData(UUID.randomUUID(), "created", LocalDateTime.now());
            AgentResponse<AgentData> response = new AgentResponse<>(true, "Success", data, null);

            assertThat(response.getSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo("Success");
            assertThat(response.getData()).isNotNull();
            assertThat(response.getError()).isNull();
        }

        @Test
        @DisplayName("AgentResponse 에러 케이스 검증") // 에러 발생 시 응답 형태 테스트
        void agentResponse_shouldHandleError() {
            AgentResponse<AgentData> response = new AgentResponse<>(false, "Failed", null, "Agent not found");

            assertThat(response.getSuccess()).isFalse();
            assertThat(response.getData()).isNull();
            assertThat(response.getError()).isEqualTo("Agent not found");
        }

        @Test
        @DisplayName("AgentData 필드 검증") // Agent 생성 응답 데이터 테스트
        void agentData_shouldHaveCorrectFields() {
            UUID agentId = UUID.randomUUID();
            LocalDateTime createdAt = LocalDateTime.now();
            AgentData data = new AgentData(agentId, "created", createdAt);

            assertThat(data.getAgentId()).isEqualTo(agentId);
            assertThat(data.getStatus()).isEqualTo("created");
            assertThat(data.getCreatedAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("FlowData 필드 검증") // Flow 응답 데이터 테스트
        void flowData_shouldContainWords() {
            List<WordItem> words = List.of(
                new WordItem(1, "Chapter 1", 1),
                new WordItem(2, "Chapter 2", 2)
            );
            FlowData flowData = new FlowData(words);

            assertThat(flowData.getWords()).hasSize(2);
        }

        @Test
        @DisplayName("DocData 필드 검증") // Doc 응답 데이터 테스트
        void docData_shouldContainDocs() {
            List<DocItem> docs = List.of(
                new DocItem("Section 1", "Content 1"),
                new DocItem("Section 2", "Content 2")
            );
            DocData docData = new DocData(docs);

            assertThat(docData.getDocs()).hasSize(2);
        }

        @Test
        @DisplayName("GraphNode 필드 검증") // 그래프 노드 데이터 테스트
        void graphNode_shouldHaveCorrectFields() {
            GraphNode node = new GraphNode(1, "Java", List.of(2, 3, 4));

            assertThat(node.getId()).isEqualTo(1);
            assertThat(node.getKeyword()).isEqualTo("Java");
            assertThat(node.getLinks()).containsExactly(2, 3, 4);
        }

        @Test
        @DisplayName("GraphData 필드 검증") // Graph 응답 데이터 테스트
        void graphData_shouldContainNodes() {
            List<GraphNode> nodes = List.of(
                new GraphNode(1, "Java", List.of(2)),
                new GraphNode(2, "OOP", List.of(1))
            );
            GraphData graphData = new GraphData(nodes);

            assertThat(graphData.getNodes()).hasSize(2);
        }

        @Test
        @DisplayName("CompleteData 필드 검증") // Complete 응답 데이터 테스트
        void completeData_shouldHaveCorrectFields() {
            UUID agentId = UUID.randomUUID();
            CompleteData data = new CompleteData(agentId, "completed", "# Final Markdown Content");

            assertThat(data.getAgentId()).isEqualTo(agentId);
            assertThat(data.getStatus()).isEqualTo("completed");
            assertThat(data.getFinalMarkdown()).isEqualTo("# Final Markdown Content");
        }

        @Test
        @DisplayName("ChatData 필드 검증") // Chat 응답 데이터 테스트
        void chatData_shouldHaveMessageAndHistory() {
            List<ChatMessage> history = List.of(
                new ChatMessage("user", "Hi"),
                new ChatMessage("assistant", "Hello!")
            );
            ChatData chatData = new ChatData("I'm doing well!", history);

            assertThat(chatData.getMessage()).isEqualTo("I'm doing well!");
            assertThat(chatData.getHistory()).hasSize(2);
        }

        @Test
        @DisplayName("AllAgentData 필드 검증") // Agent 전체 데이터 조회 응답 테스트
        void allAgentData_shouldContainAllFields() {
            UUID agentId = UUID.randomUUID();
            Planning planning = new Planning("Java", "Learn OOP", List.of("class"), List.of("link"));
            FlowData flow = new FlowData(List.of(new WordItem(1, "Ch1", 1)));
            DocData doc = new DocData(List.of(new DocItem("Sec1", "Content")));
            GraphData graph = new GraphData(List.of(new GraphNode(1, "Java", List.of())));

            AllAgentData data = new AllAgentData(agentId, "completed", planning, flow, doc, graph);

            assertThat(data.getAgentId()).isEqualTo(agentId);
            assertThat(data.getStatus()).isEqualTo("completed");
            assertThat(data.getPlanning()).isNotNull();
            assertThat(data.getFlow()).isNotNull();
            assertThat(data.getDoc()).isNotNull();
            assertThat(data.getGraph()).isNotNull();
        }

        @Test
        @DisplayName("AllAgentData Optional 필드가 null일 수 있는지 검증") // Optional 필드 null 허용 테스트
        void allAgentData_shouldAllowNullOptionalFields() {
            UUID agentId = UUID.randomUUID();
            AllAgentData data = new AllAgentData(agentId, "planning", null, null, null, null);

            assertThat(data.getAgentId()).isEqualTo(agentId);
            assertThat(data.getPlanning()).isNull();
            assertThat(data.getFlow()).isNull();
            assertThat(data.getDoc()).isNull();
            assertThat(data.getGraph()).isNull();
        }
    }
}
