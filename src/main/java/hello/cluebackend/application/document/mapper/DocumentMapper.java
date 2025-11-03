package hello.cluebackend.application.document.mapper;

import hello.cluebackend.application.document.dto.*;
import hello.cluebackend.domain.assignment.model.FileType;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.directory.model.Directory;
import hello.cluebackend.domain.document.model.Document;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DocumentMapper {
    public DocumentAllInfoDto toDocumentAllInfoDto(Document document) {
        return DocumentAllInfoDto.builder()
                .documentId(document.getDocumentId())
                .title(document.getTitle())
                .createdAt(document.getCreatedAt())
                .build();
    }

    public UrlDto toUrlDto(Document document) {
        return UrlDto.builder()
                .value(document.getValue())
                .title(document.getTitle())
                .build();
    }

    public DownloadDto toDownloadDto(Document document, Resource resource) {
        return DownloadDto.builder()
                .original(document.getOriginalFileName())
                .contentType(document.getContentType())
                .resource(resource)
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

    public Document fromRequestDocumentDtoToDocument(RequestDocumentDto requestDocumentDto, ClassRoom classRoom, Directory directory, String storedFileName, MultipartFile file) {
        return Document.builder()
                .classRoom(classRoom)
                .directory(directory)
                .title(requestDocumentDto.getTitle())
                .type(FileType.FILE)
                .value(storedFileName)
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();
    }
}
