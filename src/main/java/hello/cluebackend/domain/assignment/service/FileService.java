package hello.cluebackend.domain.assignment.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String storeFile(MultipartFile file) {
    // 1. 고유한 파일명 생성 (UUID + 원래 확장자 유지)
    String originalFilename = file.getOriginalFilename();
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    String storedFileName = UUID.randomUUID().toString() + extension;

    // 2. 메타데이터 세팅
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try {
      // 3. S3에 업로드
      amazonS3Client.putObject(new PutObjectRequest(bucket, storedFileName, file.getInputStream(), metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead)); // 공개 권한 설정

      // 4. 업로드 후 URL 반환
      return amazonS3Client.getUrl(bucket, storedFileName).toString();

    } catch (IOException e) {
      throw new RuntimeException("파일 업로드에 실패했습니다.", e);
    }
  }
}
