package hello.cluebackend.presentation.api.document;

import hello.cluebackend.application.document.dto.*;
import hello.cluebackend.domain.document.service.DocumentService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping(value = "/file")
    public ResponseEntity<Void> uploadDocument(
            @RequestPart(value = "metadata") List<RequestDocumentDto> requestDocumentDto,
            @RequestPart(value = "files") @NotNull List<MultipartFile> files,
            @RequestParam(value = "classRoomId") UUID classRoomId,
            @RequestParam(value = "directoryId") UUID directoryId) throws IOException {
        documentService.uploadFileDocument(classRoomId, directoryId, requestDocumentDto, files);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PatchMapping
    public ResponseEntity<Void> updateDocument(
            @RequestBody UpdateFileDto fileDto) {
        documentService.updateDocument(fileDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @DeleteMapping("/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable("documentId") UUID documentId) {
        documentService.deleteDocument(documentId);
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

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/link")
    public ResponseEntity<Void> urlUpload(@RequestBody InfoDto urlDto) {
        documentService.uploadUrlDocument(urlDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{documentId}/link")
    public ResponseEntity<UrlDto> linkDocument(@PathVariable("documentId") UUID documentId) {
        UrlDto urlDto = documentService.getLink(documentId);
        return ResponseEntity.status(HttpStatus.OK).body(urlDto);
    }
}
