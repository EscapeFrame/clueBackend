package hello.cluebackend.domain.document.service;

import hello.cluebackend.domain.assignment.domain.FileType;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.directory.domain.Directory;
import hello.cluebackend.domain.directory.domain.repository.DirectoryRepository;
import hello.cluebackend.domain.document.domain.Document;
import hello.cluebackend.domain.document.domain.repository.DocumentRepository;
import hello.cluebackend.domain.document.presentation.dto.*;
import hello.cluebackend.domain.file.service.FileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {

    @Value("${upload.local.dir}")
    private String uploadDir;

    private final DocumentRepository documentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final DirectoryRepository directoryRepository;
    private final FileService fileService;

    public void uploadUrlDocument(InfoDto urlDto) {
        ClassRoom classRoom = classRoomRepository.findById(urlDto.getClassRoomId()).orElseThrow(() -> new EntityNotFoundException("해당 교실을 찾을 수가 없습니다."));
        Directory directory = directoryRepository.findById(urlDto.getDirectoryId()).orElseThrow(() -> new EntityNotFoundException("해당 디렉토리를 찾을 수가 없습니다."));
        for (UrlDto dto : urlDto.getUrls()) {
            Document document = Document.builder()
                    .title(dto.getTitle())
                    .classRoom(classRoom)
                    .directory(directory)
                    .type(FileType.URL)
                    .value(dto.getValue())
                    .build();
            documentRepository.save(document);
        }
    }

    public void uploadFileDocument(UUID classRoomId, UUID directoryId, List<RequestDocumentDto> requestDocumentDto, List<MultipartFile> files) {
        ClassRoom findClassRoom = classRoomRepository.findById(classRoomId).orElseThrow(() -> new EntityNotFoundException("해당 교실을 찾을 수가 없습니다."));
        Directory findDirectory = directoryRepository.findById(directoryId).orElseThrow(() -> new EntityNotFoundException("해당 디렉토리를 찾을 수가 없습니다."));

        log.info("requestDocumentDto size: {}", requestDocumentDto.size());
        log.info("files size: {}", files.size());
        if(requestDocumentDto.size() != files.size()) {
            throw new RuntimeException("한쪽 요소 부족");
        }
        for(int i = 0; i < requestDocumentDto.size(); i++) {
            try {
                MultipartFile file = files.get(i);
                RequestDocumentDto requestDocument = requestDocumentDto.get(i);
                String storedFileName = fileService.storeFile(file);
                Document document = Document.builder()
                        .classRoom(findClassRoom)
                        .directory(findDirectory)
                        .title(requestDocument.getTitle())
                        .type(FileType.FILE)
                        .value(storedFileName)
                        .originalFileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .build();
                documentRepository.save(document);
            } catch(Exception e) {
                throw new RuntimeException("파일 저장 중 에러 발생");
            }
        }
    }

    public void updateDocument(UpdateFileDto fileDto) {
        Document document = documentRepository.findById(fileDto.getDocumentId()).orElseThrow(() -> new EntityNotFoundException("해당 자료가 존재하지 않음"));
        document.updateDetails(fileDto.getTitle());
    }

    public void deleteDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(() -> new EntityNotFoundException("해당 수업자료가 존재하지 않습니다."));
        if(document.getType() == FileType.FILE) {
            fileService.deleteFile(document.getValue());
        }
        documentRepository.delete(document);
    }

    public DocumentDto findById(UUID documentId) {
        Document findDocument = documentRepository.findById(documentId).orElseThrow(() -> new IllegalArgumentException("해당 수업자료가 존재하지 않습니다."));
        return findDocument.toDto();
    }

    public DownloadDto downloadDocument(UUID documentId) throws IOException {
        Document document = documentRepository.findById(documentId).orElseThrow(() -> new EntityNotFoundException("해당 수업자료가 존재하지 않습니다."));
        Resource resource = fileService.downloadFile(document.getValue());
        return DownloadDto.builder()
                .original(document.getOriginalFileName())
                .contentType(document.getContentType())
                .resource(resource)
                .build();
    }

    public UrlDto getLink(UUID documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(() -> new EntityNotFoundException("해당 수업자료가 존재하지 않습니다."));
        if(document.getType() == FileType.URL) {
            return UrlDto.builder()
                    .value(document.getValue())
                    .title(document.getTitle())
                    .build();
        }
        return null;
    }
}
