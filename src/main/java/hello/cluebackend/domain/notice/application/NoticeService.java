package hello.cluebackend.domain.notice.application;

import hello.cluebackend.domain.assignment.domain.FileType;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.document.presentation.dto.UrlDto;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.notice.domain.Notice;
import hello.cluebackend.domain.notice.persistence.NoticeRepository;
import hello.cluebackend.domain.notice.presentation.dto.request.AddNoticeDto;
import hello.cluebackend.domain.notice.presentation.dto.request.CreateNoticeDto;
import hello.cluebackend.domain.notice.presentation.dto.request.ModifyNoticeDto;
import hello.cluebackend.domain.notice.presentation.dto.request.NoticeFileDto;
import hello.cluebackend.domain.notice.presentation.dto.response.NoticeDto;
import hello.cluebackend.domain.notice.presentation.dto.response.NoticeInfoDto;
import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import hello.cluebackend.domain.noticedocument.persistence.NoticeDocumentRepository;
import hello.cluebackend.domain.noticedocument.presentation.dto.response.NoticeDownloadDto;
import hello.cluebackend.domain.noticedocument.presentation.dto.response.NoticeUrlDto;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeDocumentRepository noticeDocumentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @PersistenceContext
    private EntityManager em;

    public void save(UUID userId, CreateNoticeDto dto, List<MultipartFile> files) {
        // Notice
        ClassRoom classRoom = classRoomRepository.findById(dto.getClassRoomId()).orElseThrow(() -> new EntityNotFoundException("해당 교실을 찾을 수가 없습니다."));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수가 없습니다."));

        Notice notice = Notice.builder()
                .classRoom(classRoom)
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        noticeRepository.save(notice);

        em.flush();
        em.clear();

        if(dto.getFileInfo().size() != files.size()){
            throw new RuntimeException("한쪽 요소 부족");
        }

        for(int i = 0; i < dto.getFileInfo().size(); i++){
            try {
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
            } catch(Exception e) {
                throw new RuntimeException("파일 저장 중 에러 발생");
            }
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

    public void remove(UUID noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 찾을 수가 없습니다."));
        List<NoticeDocument> noticeDocuments = noticeDocumentRepository.findByNotice_NoticeId(noticeId);
        for(NoticeDocument noticeDocument : noticeDocuments){
            fileService.deleteFile(noticeDocument.getValue());
            noticeDocumentRepository.delete(noticeDocument);
        }
        noticeRepository.delete(notice);
    }

    public List<NoticeDto> findAllById(UUID userId) {
        return noticeRepository.findAllByUserId(userId).stream()
                .map(Notice::toDto).toList();
    }

    public NoticeInfoDto findById(UUID noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 찾을 수가 없습니다."));
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

    public void removeNoticeDocument(UUID noticeDocumentId) {
        NoticeDocument noticeDocument = noticeDocumentRepository.findById(noticeDocumentId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항 문서를 찾을 수가 없습니다."));
        if(noticeDocument.getType() == FileType.FILE) {
            fileService.deleteFile(noticeDocument.getValue());
        }
        noticeDocumentRepository.delete(noticeDocument);
    }

    public void modifyNotice(UUID noticeId, ModifyNoticeDto modifyNoticeDto) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 찾을 수가 없습니다."));
        notice.modify(modifyNoticeDto);
    }

    public void addNoticeDocument(UUID noticeId, AddNoticeDto dto, List<MultipartFile> files) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 찾을 수가 없습니다."));
        if(dto.getFileInfo().size() != files.size()){
            throw new RuntimeException("한쪽 요소 부족");
        }
        for(int i = 0; i < dto.getFileInfo().size(); i++){
            try {
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
            } catch(Exception e) {
                throw new RuntimeException("파일 저장 중 에러 발생");
            }
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
}
