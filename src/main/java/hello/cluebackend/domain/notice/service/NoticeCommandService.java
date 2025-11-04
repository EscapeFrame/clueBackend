package hello.cluebackend.domain.notice.service;

import hello.cluebackend.application.document.dto.UrlDto;
import hello.cluebackend.application.notice.dto.request.AddNoticeDto;
import hello.cluebackend.application.notice.dto.request.CreateNoticeDto;
import hello.cluebackend.application.notice.dto.request.ModifyNoticeDto;
import hello.cluebackend.application.notice.dto.request.NoticeFileDto;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.notice.exception.IsNotMyNoticeException;
import hello.cluebackend.domain.notice.model.Notice;
import hello.cluebackend.domain.noticedocument.domain.FileType;
import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import hello.cluebackend.domain.noticedocument.domain.repository.NoticeDocumentRepository;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.notice.NoticeJpaRepository;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Transactional
public class NoticeCommandService {

    private final NoticeJpaRepository noticeJpaRepository;
    private final NoticeDocumentRepository noticeDocumentRepository;
    private final UserJpaRepository userRepository;
    private final FileService fileService;

    @PersistenceContext
    private EntityManager em;

    public void save(UUID userId, CreateNoticeDto dto, List<MultipartFile> files) throws IOException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수가 없습니다."));

        Notice notice = Notice.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .type(dto.getType())
                .build();

        noticeJpaRepository.save(notice);

        em.flush();
        em.clear();

        files = files.stream()
                .filter(file -> !file.isEmpty())
                .collect(Collectors.toList());

        log.info("Flies size: {}", files.size());
        log.info("dto.size: {}", dto.getFileInfo().size());

        if(dto.getFileInfo().size() != files.size()){
            throw new RuntimeException("한쪽 요소 부족");
        }

        for(int i = 0; i < dto.getFileInfo().size(); i++){
            MultipartFile file = files.get(i);
            NoticeFileDto noticeFileDto = dto.getFileInfo().get(i);
            String storedFileName = fileService.storeFile(file);
            NoticeDocument noticeDocument = NoticeDocument.builder()
                    .notice(notice)
                    .title(noticeFileDto.getTitle())
                    .type(FileType.FILE)
                    .value(storedFileName)
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();
            noticeDocumentRepository.save(noticeDocument);
        }
        for(UrlDto urlDto : dto.getUrls()){
            NoticeDocument noticeDocument = NoticeDocument.builder()
                    .title(urlDto.getTitle())
                    .type(FileType.URL)
                    .value(urlDto.getValue())
                    .notice(notice)
                    .build();
            noticeDocumentRepository.save(noticeDocument);
        }
    }

    public void remove(UUID userId, UUID noticeId) {
        if(!isMyNotice(userId, noticeId)) {
            throw new IsNotMyNoticeException("해당 공지사항을 수정할 권한이 없습니다.");
        }
        Notice notice = noticeJpaRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 찾을 수가 없습니다."));
        List<NoticeDocument> noticeDocuments = noticeDocumentRepository.findByNotice_NoticeId(noticeId);
        for(NoticeDocument noticeDocument : noticeDocuments){
            fileService.deleteFile(noticeDocument.getValue());
            noticeDocumentRepository.delete(noticeDocument);
        }
        noticeJpaRepository.delete(notice);
    }

    public void removeNoticeDocument(UUID userId, UUID noticeId, UUID noticeDocumentId) {
        if(!isMyNotice(userId, noticeId)) {
            throw new IsNotMyNoticeException("해당 공지사항을 수정할 권한이 없습니다.");
        }
        NoticeDocument noticeDocument = noticeDocumentRepository.findById(noticeDocumentId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항 문서를 찾을 수가 없습니다."));
        if(noticeDocument.getType() == FileType.FILE) {
            fileService.deleteFile(noticeDocument.getValue());
        }
        noticeDocumentRepository.delete(noticeDocument);
    }

    public void modifyNotice(UUID userId, UUID noticeId, ModifyNoticeDto modifyNoticeDto) {
        if(!isMyNotice(userId, noticeId)) {
            throw new IsNotMyNoticeException("해당 공지사항을 수정할 권한이 없습니다.");
        }
        Notice notice = noticeJpaRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 찾을 수가 없습니다."));
        notice.modify(modifyNoticeDto);
    }

    public void addNoticeDocument(UUID userId, UUID noticeId, AddNoticeDto dto, List<MultipartFile> files) throws IOException {
        if(!isMyNotice(userId, noticeId)) {
            throw new IsNotMyNoticeException("해당 공지사항을 수정할 권한이 없습니다.");
        }
        Notice notice = noticeJpaRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 찾을 수가 없습니다."));
        if(dto.getFileInfo().size() != files.size()){
            throw new RuntimeException("한쪽 요소 부족");
        }
        for(int i = 0; i < dto.getFileInfo().size(); i++){
            MultipartFile file = files.get(i);
            NoticeFileDto noticeFileDto = dto.getFileInfo().get(i);
            String storedFileName = fileService.storeFile(file);
            NoticeDocument noticeDocument = NoticeDocument.builder()
                    .notice(notice)
                    .title(noticeFileDto.getTitle())
                    .type(FileType.FILE)
                    .value(storedFileName)
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();
            noticeDocumentRepository.save(noticeDocument);
        }
        for(UrlDto urlDto : dto.getUrls()){
            NoticeDocument noticeDocument = NoticeDocument.builder()
                    .title(urlDto.getTitle())
                    .type(FileType.URL)
                    .value(urlDto.getValue())
                    .notice(notice)
                    .build();
            noticeDocumentRepository.save(noticeDocument);
        }
    }

    public boolean isMyNotice(UUID userId, UUID noticeId) {
        boolean isMyNotice = noticeJpaRepository.findMyNoticeByUserId(userId, noticeId) > 0;
        log.info("isMyNotice: {}", isMyNotice);
        return isMyNotice;
    }
}
