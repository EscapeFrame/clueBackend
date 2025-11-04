package hello.cluebackend.application.notice.mapper;

import hello.cluebackend.application.document.dto.UrlDto;
import hello.cluebackend.application.notice.dto.request.CreateNoticeDto;
import hello.cluebackend.application.notice.dto.request.NoticeFileDto;
import hello.cluebackend.domain.notice.model.Notice;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeDownloadDto;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeUrlDto;
import hello.cluebackend.domain.noticedocument.domain.FileType;
import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import hello.cluebackend.domain.user.model.UserEntity;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class NoticeMapper {

    public Notice fromCreateNoticeDtoToNotice(CreateNoticeDto createNoticeDto, UserEntity user) {
        return Notice.builder()
                .user(user)
                .title(createNoticeDto.getTitle())
                .content(createNoticeDto.getContent())
                .type(createNoticeDto.getType())
                .build();
    }

    public NoticeDocument fromNoticeFileDtoToNoticeDocument(NoticeFileDto noticeFileDto, Notice notice, String storedFileName, MultipartFile file) {
        return NoticeDocument.builder()
                .notice(notice)
                .title(noticeFileDto.getTitle())
                .type(FileType.FILE)
                .value(storedFileName)
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();
    }

    public NoticeDocument fromUrlDtoToNoticeDocument(UrlDto urlDto, Notice notice) {
        return NoticeDocument.builder()
                .title(urlDto.getTitle())
                .type(FileType.URL)
                .value(urlDto.getValue())
                .notice(notice)
                .build();
    }

    public NoticeDownloadDto fromNoticeDocumentToNoticeDownloadDto(NoticeDocument noticeDocument, Resource resource) {
        return NoticeDownloadDto.builder()
                .original(noticeDocument.getValue())
                .contentType(noticeDocument.getContentType())
                .resource(resource)
                .build();
    }

    public NoticeUrlDto fromNoticeDocumentToNoticeUrlDto(NoticeDocument noticeDocument) {
        return NoticeUrlDto.builder()
                .value(noticeDocument.getValue())
                .title(noticeDocument.getTitle())
                .build();
    }
}
