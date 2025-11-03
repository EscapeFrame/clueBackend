package hello.cluebackend.presentation.api.notice;

import hello.cluebackend.application.user.dto.CustomOAuth2User;
import hello.cluebackend.application.notice.dto.response.NoticeDto;
import hello.cluebackend.application.notice.dto.response.NoticeInfoDto;
import hello.cluebackend.domain.notice.service.NoticeQueryService;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeDownloadDto;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeQueryController {

    private final NoticeQueryService noticeQueryService;

    @GetMapping
    public ResponseEntity<List<NoticeDto>> getAllNotices(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        return ResponseEntity.status(HttpStatus.OK).body(noticeQueryService.findAllById());
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeInfoDto> getNotice(
            @PathVariable UUID noticeId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        return ResponseEntity.status(HttpStatus.OK).body(noticeQueryService.findById(userId, noticeId));
    }

    @GetMapping("/download/{noticeDocumentId}")
    public ResponseEntity<Resource> downloadNoticeDocument(
            @PathVariable("noticeDocumentId")  UUID noticeDocumentId) throws IOException {
        NoticeDownloadDto dto = noticeQueryService.downloadNoticeDocument(noticeDocumentId);

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
        return ResponseEntity.status(HttpStatus.OK).body(noticeQueryService.getLink(noticeDocumentId));
    }
}
