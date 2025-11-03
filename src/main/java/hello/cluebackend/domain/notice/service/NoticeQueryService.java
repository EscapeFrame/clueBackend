
package hello.cluebackend.domain.notice.service;

import hello.cluebackend.application.document.dto.UrlDto;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.notice.model.Notice;
import hello.cluebackend.infrastructure.persistence.notice.NoticeJpaRepository;
import hello.cluebackend.application.notice.dto.request.AddNoticeDto;
import hello.cluebackend.application.notice.dto.request.CreateNoticeDto;
import hello.cluebackend.application.notice.dto.request.ModifyNoticeDto;
import hello.cluebackend.application.notice.dto.request.NoticeFileDto;
import hello.cluebackend.application.notice.dto.response.NoticeDto;
import hello.cluebackend.application.notice.dto.response.NoticeInfoDto;
import hello.cluebackend.domain.notice.exception.IsNotMyNoticeException;
import hello.cluebackend.domain.noticedocument.domain.FileType;
import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import hello.cluebackend.domain.noticedocument.domain.repository.NoticeDocumentRepository;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeDownloadDto;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeUrlDto;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeQueryService {

    private final NoticeJpaRepository noticeJpaRepository;
    private final NoticeDocumentRepository noticeDocumentRepository;
    private final FileService fileService;

    public List<NoticeDto> findAllById() {
        return noticeJpaRepository.findAll().stream()
                .map(Notice::toDto).toList();
    }

    public NoticeInfoDto findById(UUID userId, UUID noticeId) {
        if(!isMyNotice(userId, noticeId)) {
            throw new IsNotMyNoticeException("해당 공지사항을 수정할 권한이 없습니다.");
        }
        Notice notice = noticeJpaRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 찾을 수가 없습니다."));
        List<NoticeDocument> noticeDocument = notice.getNoticeDocuments();
        NoticeInfoDto noticeInfoDto = notice.toInfoDto();
        noticeInfoDto.setNoticeDocuments(noticeDocument.stream().map(NoticeDocument::toDto).toList());
        return noticeInfoDto;
    }

    public NoticeDownloadDto downloadNoticeDocument(UUID noticeDocumentId) throws IOException {
        NoticeDocument noticeDocument = noticeDocumentRepository.findById(noticeDocumentId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항 파일을 찾을 수가 없습니다."));
        if(noticeDocument.getType() == FileType.FILE){
            Resource resource = fileService.downloadFile(noticeDocument.getValue());
            return NoticeDownloadDto.builder()
                    .original(noticeDocument.getValue())
                    .contentType(noticeDocument.getContentType())
                    .resource(resource)
                    .build();
        }
        else return null;
    }

    public NoticeUrlDto getLink(UUID noticeDocumentId) {
        NoticeDocument noticeDocument = noticeDocumentRepository.findById(noticeDocumentId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항 링크를 찾을 수가 없습니다."));
        if(noticeDocument.getType() == FileType.URL) {
            return NoticeUrlDto.builder()
                    .value(noticeDocument.getValue())
                    .title(noticeDocument.getTitle())
                    .build();
        }
        return null;
    }

    public boolean isMyNotice(UUID userId, UUID noticeId) {
        boolean isMyNotice = noticeJpaRepository.findMyNoticeByUserId(userId, noticeId) > 0;
        log.info("isMyNotice: {}", isMyNotice);
        return isMyNotice;
    }
}