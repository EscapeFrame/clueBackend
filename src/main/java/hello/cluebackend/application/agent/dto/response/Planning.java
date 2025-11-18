package hello.cluebackend.application.agent.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Planning {
    @JsonProperty("studying_name")
    private String studyingName;
    @JsonProperty("learning_purpose")
    private String learningPurpose;
    @JsonProperty("main_words")
    private List<String> mainWords;
    private List<String> links;
    @JsonProperty("scraped_content")
    private String scrapedContent;
}
