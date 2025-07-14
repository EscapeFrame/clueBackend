package hello.cluebackend.domain.document.service;

import hello.cluebackend.domain.document.presentation.dto.FileUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class LocalStorageService {

    @Value("${upload.local.dir}")
    private String uploadDir;

    public String getFullPath(String fileName) {
        return uploadDir + File.separator + fileName;
    }

    public FileUpload upload(MultipartFile file) {

        String originalFileName = file.getOriginalFilename();
        String storedFileName = generateStoredFileName(originalFileName);
        String fullPath = getFullPath(storedFileName);
        File dest = new File(fullPath);

        if(!dest.getParentFile().exists()) {
            boolean created = dest.getParentFile().mkdirs();
            if(!created) {
                log.error("Unable to create directory {}", dest.getParentFile().getAbsolutePath());
                throw new RuntimeException("Directory creation failed");
            }
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("File uploading failed {}", originalFileName, e);
            throw new RuntimeException("File uploading failed " + originalFileName, e);
        }
        log.info("File uploaded {}", originalFileName);
        return FileUpload.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .build();
    }

    public List<FileUpload> storeFiles(MultipartFile[] files) {
        List<FileUpload> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                FileUpload uploadResult = upload(file);
                responses.add(uploadResult);
            } catch(Exception e) {
                log.error("Failed to store file {}: {}", file.getOriginalFilename(), e.getMessage());
            }
        }
        return responses;
    }

    // uuid_원본파일명
    private String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

}
