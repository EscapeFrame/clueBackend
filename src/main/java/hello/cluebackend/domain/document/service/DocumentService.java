package hello.cluebackend.domain.document.service;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.directory.domain.Directory;
import hello.cluebackend.domain.directory.domain.repository.DirectoryRepository;
import hello.cluebackend.domain.document.domain.Document;
import hello.cluebackend.domain.document.domain.repository.DocumentRepository;
import hello.cluebackend.domain.document.presentation.dto.DocumentDto;
import hello.cluebackend.domain.document.presentation.dto.FileUpload;
import hello.cluebackend.domain.document.presentation.dto.RequestDocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DocumentService {

    @Value("${upload.local.dir}")
    private String uploadDir;

    private final DocumentRepository documentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final DirectoryRepository directoryRepository;

    public DocumentService(DocumentRepository documentRepository, ClassRoomRepository classRoomRepository, DirectoryRepository directoryRepository) {
        this.documentRepository = documentRepository;
        this.classRoomRepository = classRoomRepository;
        this.directoryRepository = directoryRepository;
    }

    public void storeFiles(Long classRoomId, Long directoryId, List<RequestDocumentDto> requestDocumentDto, List<MultipartFile> files) {
        ClassRoom findClassRoom = classRoomRepository.findById(classRoomId).orElseThrow(() -> new IllegalArgumentException("해당 교실을 찾을 수가 없습니다."));
        Directory findDirectory = directoryRepository.findById(directoryId).orElseThrow(() -> new IllegalArgumentException("해당 디렉토를을 찾을 수가 없습니다."));

        log.info("requestDocumentDto size: {}", requestDocumentDto.size());
        log.info("files size: {}", files.size());
        if(requestDocumentDto.size() != files.size()) {
            throw new RuntimeException("한쪽 요소 부족");
        }
        for(int i = 0; i < requestDocumentDto.size(); i++) {
            try {
                FileUpload uploadResult = upload(files.get(i));
                Document document = Document.builder()
                        .classRoom(findClassRoom)
                        .directory(findDirectory)
                        .title(requestDocumentDto.get(i).getTitle())
                        .type(requestDocumentDto.get(i).getType())
                        .content(uploadResult.getFullPath())
                        .build();
                documentRepository.save(document);
            } catch(Exception e) {
                log.error("Failed to store file {}: {}", files.get(i).getOriginalFilename(), e.getMessage());
            }
        }
    }

    public String getFullPath(String fileName) {
        return uploadDir + File.separator + fileName;
    }

    public FileUpload upload(MultipartFile file) {

        String originalFileName = file.getOriginalFilename();
        String storedFileName = generateStoredFileName(originalFileName);
        String fullPath = getFullPath(storedFileName);
        File dest = new File(fullPath);

        if(!dest.getParentFile().exists()) {
            boolean created = dest.getParentFile().mkdirs();
            if(!created) {
                log.error("Unable to create directory {}", dest.getParentFile().getAbsolutePath());
                throw new RuntimeException("Directory creation failed");
            }
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("File uploading failed {}", originalFileName, e);
            throw new RuntimeException("File uploading failed " + originalFileName, e);
        }
        log.info("File uploaded {}", originalFileName);
        return FileUpload.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .fullPath(fullPath)
                .build();
    }

    // uuid_원본파일명
    private String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    public void deleteById(Long documentId) {
        Document findDocument = documentRepository.findById(documentId).orElseThrow(() -> new IllegalArgumentException("해당 수업을 찾지 못했습니다."));
        String fullPath = findDocument.getContent();

        File  file = new File(fullPath);
        if(file.exists()) {
            boolean deleted = file.delete();
            if(!deleted) {
                log.warn("파일 삭제 실패: {}", fullPath);
            }
        } else {
            log.warn("파일이 존재하지 않음: {}", fullPath);
        }
        try {
            documentRepository.deleteById(documentId);
        } catch(Exception e) {
            log.error("Failed to delete document {}", documentId, e);
            throw new RuntimeException("수업자료 삭제 실패 " + documentId, e);
        }

    }

    public DocumentDto findById(Long documentId) {
        Document findDocument = documentRepository.findById(documentId).orElseThrow(() -> new IllegalArgumentException("해당 수업자료가 존재하지 않습니다."));
        return findDocument.toDto();
    }

    public void updateDocument(Long classRoomId, Long directoryId, List<RequestDocumentDto> requestDocumentDto, List<MultipartFile> files) {
        System.out.println("directoryId = " + directoryId);
        ClassRoom findClassRoom = classRoomRepository.findById(classRoomId).orElseThrow(() -> new IllegalArgumentException("해당 교실을 찾을 수가 없습니다."));
        Directory findDirectory = directoryRepository.findById(directoryId).orElseThrow(() -> new IllegalArgumentException("해당 디렉토를을 찾을 수가 없습니다."));
        System.out.println("통과");
        if(requestDocumentDto.size() != files.size()) {
            throw new RuntimeException("한쪽 요소 부족");
        }

        for(int i = 0; i < requestDocumentDto.size(); i++) {
            Document findDocument = documentRepository.findById(requestDocumentDto.get(i).getDocumentId()).orElseThrow(() -> new IllegalArgumentException("해당 수업을 찾지 못했습니다."));
            String fullPath = findDocument.getContent();

            File file = new File(fullPath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    log.warn("파일 삭제 실패: {}", fullPath);
                }
            } else {
                log.warn("파일이 존재하지 않음: {}", fullPath);
            }

            try {
                FileUpload uploadResult = upload(files.get(i));
                findDocument.setTitle(requestDocumentDto.get(i).getTitle());
                findDocument.setType(requestDocumentDto.get(i).getType());
                findDocument.setContent(uploadResult.getFullPath());
                documentRepository.save(findDocument);
            } catch(Exception e) {
                log.error("Failed to store file {}: {}", files.get(i).getOriginalFilename(), e.getMessage());
            }
        }

    }
}
