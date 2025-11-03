package hello.cluebackend.domain.notice.controller;

import hello.cluebackend.application.user.dto.CustomOAuth2User;
import hello.cluebackend.domain.notice.service.NoticeService;
import hello.cluebackend.domain.notice.controller.dto.request.AddNoticeDto;
import hello.cluebackend.domain.notice.controller.dto.request.CreateNoticeDto;
import hello.cluebackend.domain.notice.controller.dto.request.ModifyNoticeDto;
import hello.cluebackend.domain.notice.controller.dto.response.NoticeDto;
import hello.cluebackend.domain.notice.controller.dto.response.NoticeInfoDto;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeDownloadDto;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<Void> createNotice(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestPart(value = "metadata") CreateNoticeDto createNoticeDto,
            @RequestPart(value = "files") List<MultipartFile> files
            ) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();

        noticeService.save(userId, createNoticeDto, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{noticeId}")
    public ResponseEntity<Void> addNoticeDocument(
            @PathVariable("noticeId") UUID noticeId,
            @RequestPart(value = "metadata") AddNoticeDto addNoticeDto,
            @RequestPart(value = "files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        noticeService.addNoticeDocument(userId, noticeId, addNoticeDto, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PatchMapping("/{noticeId}")
    public ResponseEntity<Void> updateNotice(
            @PathVariable("noticeId") UUID noticeId,
            @RequestBody ModifyNoticeDto modifyNoticeDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        noticeService.modifyNotice(userId, noticeId, modifyNoticeDto);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable("noticeId") UUID noticeId) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        noticeService.remove(userId, noticeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<NoticeDto>> getAllNotices(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        return ResponseEntity.status(HttpStatus.OK).body(noticeService.findAllById());
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeInfoDto> getNotice(
            @PathVariable UUID noticeId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        return ResponseEntity.status(HttpStatus.OK).body(noticeService.findById(userId, noticeId));
    }

    @GetMapping("/download/{noticeDocumentId}")
    public ResponseEntity<Resource> downloadNoticeDocument(
            @PathVariable("noticeDocumentId")  UUID noticeDocumentId) throws IOException {
        NoticeDownloadDto dto = noticeService.downloadNoticeDocument(noticeDocumentId);

        String original = dto.getOriginal();
        String contentType = dto.getContentType();
        MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.ALL;

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(original, StandardCharsets.UTF_8)
                        .build()
                        .toString())
                .body(dto.getResource());
    }

    @GetMapping("/link/{noticeDocumentId}")
    public ResponseEntity<NoticeUrlDto> getNoticeLink(
            @PathVariable("noticeDocumentId") UUID noticeDocumentId) {
        return ResponseEntity.status(HttpStatus.OK).body(noticeService.getLink(noticeDocumentId));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{noticeId}/document/{noticeDocumentId}")
    public ResponseEntity<Void> deleteNoticeDocument(
            @PathVariable("noticeId") UUID noticeId,
            @PathVariable("noticeDocumentId") UUID noticeDocumentId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        noticeService.removeNoticeDocument(userId, noticeId, noticeDocumentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}