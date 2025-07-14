package hello.cluebackend.domain.document.presentation;

import com.nimbusds.jose.util.Resource;
import hello.cluebackend.domain.document.presentation.dto.DocumentDto;
import hello.cluebackend.domain.document.presentation.dto.FileUpload;
import hello.cluebackend.domain.document.presentation.dto.RequestDocumentDto;
import hello.cluebackend.domain.document.service.DocumentService;
import hello.cluebackend.domain.document.service.LocalStorageService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/document")
public class DocumentController {

    private final JWTUtil jwtUtil;
    private final LocalStorageService localStorageService;
    private final DocumentService documentService;

    public DocumentController(JWTUtil jwtUtil, LocalStorageService localStorageService,  DocumentService documentService) {
        this.jwtUtil = jwtUtil;
        this.localStorageService = localStorageService;
        this.documentService = documentService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadDocument(
            @RequestPart("metadata") List<RequestDocumentDto> requestDocumentDto,
            @RequestPart("files")  List<MultipartFile> files,
            @RequestPart("classRoomId") Long classRoomId,
            @RequestPart("directoryId") Long directoryId,
            HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);

        if(role != Role.TEACHER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            System.out.println("requestDocumentDto = " + requestDocumentDto);
            documentService.storeFiles(classRoomId, directoryId, requestDocumentDto, files);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDocument(@RequestBody RequestDocumentDto requestDocumentDto, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);

        if(role != Role.TEACHER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            documentService.deleteById(requestDocumentDto.getDocumentId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadDocument(@RequestParam("documentId") Long documentId) {

        try {
            DocumentDto documentDto = documentService.findById(documentId);
            String fullPath = documentDto.getContent();
            UrlResource resource = new UrlResource("file:" + fullPath);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/test")
    public ResponseEntity<List<FileUpload>> uploadMultipartFileTest(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);

        if(role != Role.TEACHER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<FileUpload> fileUploads = localStorageService.storeFiles(files);
        return ResponseEntity.ok(fileUploads);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable String filename) throws MalformedURLException {
        String fullPath = localStorageService.getFullPath(filename);
        UrlResource resource = new UrlResource("file:" + fullPath);

        String encodedFileName = UriUtils.encode(filename, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
