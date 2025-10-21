package hello.cluebackend.domain.student.document.controller;

import hello.cluebackend.domain.document.presentation.dto.*;
import hello.cluebackend.domain.student.document.controller.dto.*;
import hello.cluebackend.domain.student.document.service.DocumentService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/file")
    public ResponseEntity<Void> uploadDocument(
            @RequestPart(value = "metadata") List<RequestDocumentDto> requestDocumentDto,
            @RequestPart(value = "files")  List<MultipartFile> files,
            @RequestParam(value = "classRoomId") UUID classRoomId,
            @RequestParam(value = "directoryId") UUID directoryId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Role role = customOAuth2User.getUserDTO().getRole();

        if(role == Role.TEACHER) {
            documentService.uploadFileDocument(classRoomId, directoryId, requestDocumentDto, files);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PatchMapping
    public ResponseEntity<Void> updateDocument(
            @RequestBody UpdateFileDto fileDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Role role = customOAuth2User.getUserDTO().getRole();

        if(role == Role.TEACHER) {
            documentService.updateDocument(fileDto);
            return ResponseEntity.ok().build();
        }
        else {
            throw new AuthorizationDeniedException("권한이 부족합니다.");
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable("documentId") UUID documentId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Role role = customOAuth2User.getUserDTO().getRole();

        if(role != Role.TEACHER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            documentService.deleteDocument(documentId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable("documentId") UUID documentId) throws IOException {

        DownloadDto dto = documentService.downloadDocument(documentId);

        String original = dto.getOriginal();
        String contentType = dto.getContentType();
        MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.ALL;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(original, StandardCharsets.UTF_8)
                        .build()
                        .toString())
                .body(dto.getResource());
    }

    @PostMapping("/link")
    public ResponseEntity<Void> urlUpload(@RequestBody InfoDto urlDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        documentService.uploadUrlDocument(urlDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{documentId}/link")
    public ResponseEntity<UrlDto> linkDocument(@PathVariable("documentId") UUID documentId) {
        UrlDto urlDto = documentService.getLink(documentId);
        return ResponseEntity.status(HttpStatus.OK).body(urlDto);
    }
}
