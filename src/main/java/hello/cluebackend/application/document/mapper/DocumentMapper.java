package hello.cluebackend.application.document.mapper;

import hello.cluebackend.application.document.dto.DocumentAllInfoDto;
import hello.cluebackend.application.document.dto.DocumentDto;
import hello.cluebackend.application.document.dto.DownloadDto;
import hello.cluebackend.application.document.dto.UrlDto;
import hello.cluebackend.domain.assignment.model.FileType;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.directory.model.Directory;
import hello.cluebackend.domain.document.model.Document;
import org.springframework.core.io.Resource;
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

    public Document fromUrlDtoToDocument(UrlDto urlDto, ClassRoom classRoom, Directory directory) {
        return Document.builder()
                .title(urlDto.getTitle())
                .classRoom(classRoom)
                .directory(directory)
                .type(FileType.URL)
                .value(urlDto.getValue())
                .build();
    }

    public DownloadDto toDownloadDto(Document document, Resource resource) {
        return DownloadDto.builder()
                .original(document.getOriginalFileName())
                .contentType(document.getContentType())
                .resource(resource)
                .build();
    }
}
