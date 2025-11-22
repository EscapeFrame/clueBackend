package hello.cluebackend.infrastructure.client.quiz;

import hello.cluebackend.application.agent.dto.response.AgentResponse;
import hello.cluebackend.application.quizbattle.dto.QuizGenerationRequest;
import hello.cluebackend.application.quizbattle.dto.QuizGenerationResponse;
import hello.cluebackend.config.FeignLoggerConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for FastAPI quiz generation service
 * Calls RAG-based quiz generation endpoint
 */
@FeignClient(name = "quizClient", url = "${fastapi.url}", configuration = FeignLoggerConfig.class)
public interface QuizClient {

    /**
     * Generate quiz questions using RAG (Retrieval-Augmented Generation)
     * FastAPI endpoint should implement this endpoint to generate questions
     */
    @PostMapping("/api/v1/quiz/generate")
    AgentResponse<QuizGenerationResponse> generateQuiz(@RequestBody QuizGenerationRequest request);
}
