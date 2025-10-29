package hello.cluebackend.application.document.mapper;

import hello.cluebackend.application.document.dto.DocumentAllInfoDto;
import hello.cluebackend.domain.document.model.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {
    public DocumentAllInfoDto toDocumentAllInfoDto(Document document) {
        return DocumentAllInfoDto.builder()
                .documentId(document.getDocumentId())
                .title(document.getTitle())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
