package hello.cluebackend.infrastructure.client.linksave;

import hello.cluebackend.application.linksave.dto.request.LinkRequest;
import hello.cluebackend.application.linksave.dto.response.LinkResponse;
import hello.cluebackend.config.FeignOkHttpConfiguration;
import hello.cluebackend.domain.linksave.model.AuthorizationType;
import hello.cluebackend.domain.linksave.model.SubjectType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name="LinkSave", url = "${linksave.url}", configuration = FeignOkHttpConfiguration.class)
public interface LinkSaveClient {

    @GetMapping("/linksave")
    List<LinkResponse> getAll(
            @RequestParam UUID userId,
            @RequestParam int grade,
            @RequestParam int clas,
            @RequestParam(required = false) SubjectType subjectType,
            @RequestParam(required = false) AuthorizationType authorizationType,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(defaultValue = "0") int offset
    );

    @GetMapping("/linksave/{link_id}") // 링크 단일 조회
    LinkResponse getLink(
            @RequestParam UUID userId,
            @RequestParam int grade,
            @RequestParam int clas,
            @PathVariable Long link_id
    );

    @PostMapping("/linksave")
    LinkResponse save(
            @RequestParam UUID userId,
            @RequestBody LinkRequest linkRequest
    );

    @DeleteMapping("/linksave/{link_id}") // 링크 삭제
    void deleteLink(
            @RequestParam UUID userId,
            @PathVariable Long link_id
    );

    @PatchMapping("/linksave/{link_id}") // 링크 수정
    LinkResponse updateLink(
            @RequestParam UUID userId,
            @PathVariable Long link_id,
            @RequestBody LinkRequest linkRequest
    );
}