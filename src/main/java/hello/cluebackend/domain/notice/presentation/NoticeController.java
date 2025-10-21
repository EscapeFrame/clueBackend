package hello.cluebackend.domain.notice.presentation;

import hello.cluebackend.domain.notice.application.NoticeService;
import hello.cluebackend.domain.notice.presentation.dto.request.CreateNoticeDto;
import hello.cluebackend.domain.notice.presentation.dto.request.NoticeFileDto;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;


    @PostMapping
    public ResponseEntity<?> createNotice(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestPart(value = "metadata") CreateNoticeDto createNoticeDto,
            @RequestPart(value = "files") List<MultipartFile> files
            ) {
        UUID userId = customOAuth2User.getUserDTO().getUserId();
        Role role = customOAuth2User.getUserDTO().getRole();

        if(role == Role.TEACHER) {
            noticeService.save(userId, createNoticeDto, files);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
