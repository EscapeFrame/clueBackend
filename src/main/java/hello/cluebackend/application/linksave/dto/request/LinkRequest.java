package hello.cluebackend.application.linksave.dto.request;

import hello.cluebackend.domain.linksave.model.AuthorizationType;
import hello.cluebackend.domain.linksave.model.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkRequest {
    private char grade;
    private char clas;
    private String title;
    private String description;
    private String link;
    private AuthorizationType authorizationType;
    private SubjectType subjectType;
}