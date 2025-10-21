package hello.cluebackend.domain.notice.application;

import hello.cluebackend.domain.notice.persistence.NoticeRepository;
import hello.cluebackend.domain.notice.presentation.dto.request.CreateNoticeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public void save(UUID userId, CreateNoticeDto createNoticeDto, List<MultipartFile> files) {

    }
}
