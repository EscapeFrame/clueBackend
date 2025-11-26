package hello.cluebackend.application.linksave.dto.response;

import hello.cluebackend.domain.linksave.model.AuthorizationType;
import hello.cluebackend.domain.linksave.model.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LinkResponse {
    private Long id;
    private int grade;
    private int clas;
    private String title;
    private String description;
    private String link;
    private AuthorizationType authorizationType;
    private SubjectType subjectType;
    private boolean isMine;
}
