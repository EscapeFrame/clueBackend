package hello.cluebackend.domain.document.service;

import hello.cluebackend.application.document.dto.*;
import hello.cluebackend.application.document.mapper.DocumentMapper;
import hello.cluebackend.domain.assignment.model.FileType;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.domain.directory.model.Directory;
import hello.cluebackend.infrastructure.persistence.directory.DirectoryJpaRepository;
import hello.cluebackend.domain.document.model.Document;
import hello.cluebackend.infrastructure.persistence.document.DocumentJpaRepository;
import hello.cluebackend.domain.file.service.FileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

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

    private final DocumentJpaRepository documentJpaRepository;
    private final ClassRoomJpaRepository classRoomJpaRepository;
    private final DirectoryJpaRepository directoryJpaRepository;
    private final FileService fileService;
    private final DocumentMapper documentMapper;

    public void uploadUrlDocument(InfoDto urlDto) {
        ClassRoom classRoom = classRoomJpaRepository.findById(urlDto.getClassRoomId()).orElseThrow(() -> new EntityNotFoundException("해당 교실을 찾을 수가 없습니다."));
        Directory directory = directoryJpaRepository.findById(urlDto.getDirectoryId()).orElseThrow(() -> new EntityNotFoundException("해당 디렉토리를 찾을 수가 없습니다."));
        for (UrlDto dto : urlDto.getUrls()) {
            Document document = documentMapper.fromUrlDtoToDocument(dto, classRoom, directory);
            documentJpaRepository.save(document);
        }
    }

    public void uploadFileDocument(UUID classRoomId, UUID directoryId, List<RequestDocumentDto> requestDocumentDto, List<MultipartFile> files) {
        ClassRoom findClassRoom = classRoomJpaRepository.findById(classRoomId).orElseThrow(() -> new EntityNotFoundException("해당 교실을 찾을 수가 없습니다."));
        Directory findDirectory = directoryJpaRepository.findById(directoryId).orElseThrow(() -> new EntityNotFoundException("해당 디렉토리를 찾을 수가 없습니다."));

        validateFileSize(requestDocumentDto, files);
        for(int i = 0; i < requestDocumentDto.size(); i++) {
            try {
                MultipartFile file = files.get(i);
                RequestDocumentDto requestDocument = requestDocumentDto.get(i);
                String storedFileName = fileService.storeFile(file);
                Document document = documentMapper.fromRequestDocumentDtoToDocument(requestDocument,findClassRoom, findDirectory, storedFileName, file);
                documentJpaRepository.save(document);
            } catch(Exception e) {
                throw new RuntimeException("파일 저장 중 에러 발생");
            }
        }
    }

    public void updateDocument(UpdateFileDto fileDto) {
        Document document = documentJpaRepository.findById(fileDto.getDocumentId()).orElseThrow(() -> new EntityNotFoundException("해당 자료가 존재하지 않음"));
        document.updateDetails(fileDto.getTitle());
    }

    public void deleteDocument(UUID documentId) {
        Document document = documentJpaRepository.findById(documentId).orElseThrow(() -> new EntityNotFoundException("해당 수업자료가 존재하지 않습니다."));
        if(document.getType() == FileType.FILE) {
            fileService.deleteFile(document.getValue());
        }
        documentJpaRepository.delete(document);
    }

    public DownloadDto downloadDocument(UUID documentId) throws IOException {
        Document document = documentJpaRepository.findById(documentId).orElseThrow(() -> new EntityNotFoundException("해당 수업자료가 존재하지 않습니다."));
        Resource resource = fileService.downloadFile(document.getValue());
        return documentMapper.toDownloadDto(document, resource);
    }

    public UrlDto getLink(UUID documentId) {
        Document document = documentJpaRepository.findById(documentId).orElseThrow(() -> new EntityNotFoundException("해당 수업자료가 존재하지 않습니다."));
        if(document.getType() == FileType.URL) {
            return documentMapper.toUrlDto(document);
        }
        return null;
    }

    public void validateFileSize(List<RequestDocumentDto> requestDocumentDto, List<MultipartFile> files) {
        if(requestDocumentDto.size() != files.size()) {
            throw new RuntimeException("한쪽 요소 부족");
        }
    }
}
