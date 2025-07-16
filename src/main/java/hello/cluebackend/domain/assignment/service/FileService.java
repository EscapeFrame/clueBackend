package hello.cluebackend.domain.assignment.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
  private final AmazonS3Client amazonS3Client;
  private final AssignmentAttachmentRepository assignmentAttachmentRepository;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String storeFile(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    String storedFileName = UUID.randomUUID().toString() + extension;

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try {
      amazonS3Client.putObject(new PutObjectRequest(bucket, storedFileName, file.getInputStream(), metadata)); // 공개 권한 설정

      return amazonS3Client.getUrl(bucket, storedFileName).toString();

    } catch (IOException e) {
      throw new RuntimeException("파일 업로드에 실패했습니다.", e);
    }
  }

  public Resource downloadFile(Long attachmentId, Long userId) {
    AssignmentAttachment attachment = assignmentAttachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

    String filePath = attachment.getFilePath();

    S3Object s3Object = amazonS3Client.getObject(bucket, filePath);

    return new InputStreamResource(s3Object.getObjectContent());
  }
}
