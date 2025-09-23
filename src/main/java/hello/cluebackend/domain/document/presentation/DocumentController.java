package hello.cluebackend.domain.document.presentation;

import hello.cluebackend.domain.document.presentation.dto.DocumentDto;
import hello.cluebackend.domain.document.presentation.dto.RequestDocumentDto;
import hello.cluebackend.domain.document.presentation.dto.UpdateFileDto;
import hello.cluebackend.domain.document.presentation.dto.DeleteDocumentDto;
import hello.cluebackend.domain.document.service.DocumentService;
import hello.cluebackend.domain.document.service.LocalStorageService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {

    private final JWTUtil jwtUtil;
    private final LocalStorageService localStorageService;
    private final DocumentService documentService;

    @PostMapping(value = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> uploadDocument(
            @RequestPart(value = "metadata") List<RequestDocumentDto> requestDocumentDto,
            @RequestPart(value = "files")  List<MultipartFile> files,
            @RequestPart(value = "classRoomId") UUID classRoomId,
            @RequestPart(value = "directoryId") UUID directoryId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Role role = customOAuth2User.getUserDTO().getRole();

        if(role != Role.TEACHER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            documentService.uploadFileDocument(classRoomId, directoryId, requestDocumentDto, files);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateDocument(
            @RequestPart("metadata") List<UpdateFileDto> fileDto,
            HttpServletRequest request) {

        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);

        if(role != Role.TEACHER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
//            UUID documentId, RequestDocumentDto requestDocumentDto
            documentService.updateDocument(fileDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDocument(DeleteDocumentDto deleteDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Role role = customOAuth2User.getUserDTO().getRole();

        if(role != Role.TEACHER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            documentService.deleteDocument(deleteDto.getDocumentId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<UrlResource> downloadDocument(@PathVariable("documentId") UUID documentId) {

        try {
            DocumentDto documentDto = documentService.findById(documentId);
            String fullPath = documentDto.getContent();
            UrlResource resource = new UrlResource("file:" + fullPath);

            String encodedFileName = UriUtils.encode(fullPath.split("_")[1], StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
        } catch (MalformedURLException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
