package hello.cluebackend.presentation.api.linksave;

import hello.cluebackend.application.linksave.dto.request.LinkRequest;
import hello.cluebackend.application.linksave.dto.response.LinkResponse;
import hello.cluebackend.domain.linksave.model.AuthorizationType;
import hello.cluebackend.domain.linksave.model.SubjectType;
import hello.cluebackend.infrastructure.client.linksave.LinkSaveClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/linksave")
@RequiredArgsConstructor
public class LinkSaveController {

    private final LinkSaveClient linkSaveClient;

    @GetMapping // 링크 전체 조회
    public ResponseEntity<List<LinkResponse>> getAll(
            @RequestParam char grade,
            @RequestParam char clas,
            @RequestParam AuthorizationType authorization,
            @RequestParam() SubjectType subjectType,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(defaultValue = "0") int offset
    ){
        List<LinkResponse> linkResponses = linkSaveClient.getAll(grade,clas,authorization,subjectType,size,offset);
        return ResponseEntity.ok(linkResponses);
    }

    @GetMapping("/{linkId}") // 링크 단일 조회
    public ResponseEntity<LinkResponse> getAllLink(@PathVariable Long linkId){
        return ResponseEntity.status(HttpStatus.OK).body(linkSaveClient.getLink(linkId));
    }

    @PostMapping
    public ResponseEntity<LinkResponse> save(@RequestBody LinkRequest linkRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(linkSaveClient.save(linkRequest));
    }

    @DeleteMapping("/{linkId}")
    public ResponseEntity<Boolean> deleteLink(@PathVariable Long linkId) {
        return ResponseEntity.status(HttpStatus.OK).body(linkSaveClient.deleteLink(linkId));
    }

    @PatchMapping("/{linkId}") // 링크 수정
    public ResponseEntity<LinkResponse> updateLink(@PathVariable Long linkId, @RequestBody LinkRequest linkRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(linkSaveClient.updateLink(linkId, linkRequest));
    }
}