package hello.cluebackend.presentation.api.notice;

import hello.cluebackend.application.user.dto.CustomOAuth2User;
import hello.cluebackend.domain.notice.service.NoticeService;
import hello.cluebackend.domain.notice.controller.dto.request.AddNoticeDto;
import hello.cluebackend.domain.notice.controller.dto.request.CreateNoticeDto;
import hello.cluebackend.domain.notice.controller.dto.request.ModifyNoticeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeCommandController {

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