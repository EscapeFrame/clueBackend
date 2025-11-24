package hello.cluebackend.presentation.api.linksave;

import hello.cluebackend.application.linksave.dto.request.LinkRequest;
import hello.cluebackend.application.linksave.dto.response.LinkResponse;
import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
import hello.cluebackend.domain.linksave.LinkSaveService;
import hello.cluebackend.domain.linksave.model.AuthorizationType;
import hello.cluebackend.domain.linksave.model.SubjectType;
import hello.cluebackend.domain.user.service.UserService;
import hello.cluebackend.infrastructure.client.linksave.LinkSaveClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/linksave")
@RequiredArgsConstructor
@Slf4j
public class LinkSaveController {

    private final LinkSaveClient linkSaveClient;
    private final LinkSaveService linkSaveService;

    @GetMapping // 링크 전체 조회
    public ResponseEntity<List<LinkResponse>> getAll(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam(required = false) AuthorizationType authorizationType,
            @RequestParam(required = false) SubjectType subjectType,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(defaultValue = "0") int offset
            ){
<<<<<<< HEAD
        List<LinkResponse> linkResponses = linkSaveService.getAll(customOAuth2User.getUserDTO().getUserId(),subjectType,authorizationType,size,offset);
=======
        List<LinkResponse> linkResponses = linkSaveService.getAll(customOAuth2User.getUserDTO().getUserId(),subjectType,size,offset);
>>>>>>> 099074e16c39fb62bbc7f2dc8cd7dbefe9f21417
        return ResponseEntity.ok(linkResponses);
    }

    @GetMapping("/{linkId}") // 링크 단일 조회
    public ResponseEntity<LinkResponse> getAllLink(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long linkId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(linkSaveClient.getLink(customOAuth2User.getUserId(),customOAuth2User.getUserDTO().getGrade(), customOAuth2User.getUserDTO().getClassNo(), linkId));
    }

    @PostMapping
    public ResponseEntity<LinkResponse> save(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody LinkRequest linkRequest
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(linkSaveClient.save(customOAuth2User.getUserDTO().getUserId(),linkRequest));
    }

    @DeleteMapping("/{linkId}")
    public ResponseEntity<Void> deleteLink(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long linkId
    ) {
        linkSaveClient.deleteLink(customOAuth2User.getUserId(),linkId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{linkId}") // 링크 수정
    public ResponseEntity<LinkResponse> updateLink(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long linkId,
            @RequestBody LinkRequest linkRequest
    ) {
        LinkResponse linkResponse = linkSaveClient.updateLink(customOAuth2User.getUserId(),linkId, linkRequest);
        return ResponseEntity.status(HttpStatus.OK).body(linkResponse);
    }
}