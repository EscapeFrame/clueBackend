package hello.cluebackend.infrastructure.client.linksave;

import hello.cluebackend.application.linksave.dto.request.LinkRequest;
import hello.cluebackend.application.linksave.dto.response.LinkResponse;
import hello.cluebackend.domain.linksave.model.AuthorizationType;
import hello.cluebackend.domain.linksave.model.SubjectType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="LinkSave", url = "http://localhost:8081")
public interface LinkSaveClient {

    @GetMapping("/linksave")
    List<LinkResponse> getAll(
            @RequestParam char grade,
            @RequestParam char clas,
            @RequestParam AuthorizationType authorization,
            @RequestParam() SubjectType subjectType,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(defaultValue = "0") int offset
    );

    @GetMapping("/linksave/{link_id}") // 링크 단일 조회
    LinkResponse getLink(@PathVariable Long link_id);

    @PostMapping("/linksave")
    LinkResponse save(@RequestBody LinkRequest linkRequest);

    @DeleteMapping("/linksave/{link_id}") // 링크 삭제
    boolean deleteLink(@PathVariable Long link_id);

    @PatchMapping("/linksave/{link_id}") // 링크 수정
    LinkResponse updateLink(@PathVariable Long link_id, @RequestBody LinkRequest linkRequest);
}
