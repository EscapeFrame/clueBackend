package hello.cluebackend.application.agent.dto.request;

import hello.cluebackend.application.agent.dto.response.DocItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocFeedbackRequest {
    private List<DocItem> docs;
}
