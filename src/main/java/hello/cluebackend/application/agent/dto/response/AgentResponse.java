package hello.cluebackend.application.agent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse<T> {
    private T data;
    private String message;
}
