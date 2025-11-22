package hello.cluebackend.domain.notice.service;

import hello.cluebackend.application.notice.mapper.NoticeMapper;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.notice.model.Notice;
import hello.cluebackend.infrastructure.persistence.notice.NoticeJpaRepository;
import hello.cluebackend.application.notice.dto.response.NoticeDto;
import hello.cluebackend.application.notice.dto.response.NoticeInfoDto;
import hello.cluebackend.domain.notice.exception.IsNotMyNoticeException;
import hello.cluebackend.domain.noticedocument.domain.FileType;
import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import hello.cluebackend.domain.noticedocument.domain.repository.NoticeDocumentRepository;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeDownloadDto;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeUrlDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeQueryService {

    private final NoticeJpaRepository noticeJpaRepository;
    private final NoticeDocumentRepository noticeDocumentRepository;
    private final FileService fileService;
    private final NoticeMapper noticeMapper;

    public List<NoticeDto> findAllById() {
        return noticeJpaRepository.findAll().stream()
                .map(Notice::toDto).toList();
    }

    public NoticeInfoDto findById(UUID userId, UUID noticeId) {
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
            return noticeMapper.fromNoticeDocumentToNoticeDownloadDto(noticeDocument, resource);
        }
        else return null;
    }

    public NoticeUrlDto getLink(UUID noticeDocumentId) {
        NoticeDocument noticeDocument = noticeDocumentRepository.findById(noticeDocumentId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항 링크를 찾을 수가 없습니다."));
        if(noticeDocument.getType() == FileType.URL) {
            return noticeMapper.fromNoticeDocumentToNoticeUrlDto(noticeDocument);
        }
        return null;
    }

    public boolean isMyNotice(UUID userId, UUID noticeId) {
        boolean isMyNotice = noticeJpaRepository.findMyNoticeByUserId(userId, noticeId) > 0;
        log.info("isMyNotice: {}", isMyNotice);
        return isMyNotice;
    }
}