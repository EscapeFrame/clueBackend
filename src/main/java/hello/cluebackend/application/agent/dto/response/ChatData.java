package hello.cluebackend.application.agent.dto.response;

import hello.cluebackend.application.agent.dto.request.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatData {
    private String message;
    private List<ChatMessage> history;
}
