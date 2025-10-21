package hello.cluebackend.domain.notice.application;

import hello.cluebackend.domain.assignment.domain.FileType;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.document.presentation.dto.UrlDto;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.notice.domain.Notice;
import hello.cluebackend.domain.notice.persistence.NoticeRepository;
import hello.cluebackend.domain.notice.presentation.dto.request.CreateNoticeDto;
import hello.cluebackend.domain.notice.presentation.dto.request.NoticeFileDto;
import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import hello.cluebackend.domain.noticedocument.persistence.NoticeDocumentRepository;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeDocumentRepository noticeDocumentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

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
