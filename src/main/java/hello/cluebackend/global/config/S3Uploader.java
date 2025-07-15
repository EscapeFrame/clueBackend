package hello.cluebackend.global.config;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3Client amazonS3Client;

  public String upload(MultipartFile file, String dirName) {
    String originalName = file.getOriginalFilename();
    String fileName = createFileName(originalName);
    String filePath = dirName + "/" + fileName;

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try (InputStream inputStream = file.getInputStream()) {
      amazonS3Client.putObject(bucket, filePath, inputStream, metadata);
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드 실패", e);
    }

    return amazonS3Client.getUrl(bucket, filePath).toString(); // 업로드된 파일 URL 반환
  }

  private String createFileName(String originalName) {
    String ext = originalName.substring(originalName.lastIndexOf("."));
    return UUID.randomUUID().toString() + ext;
  }

  public void delete(String filePath) {
    amazonS3Client.deleteObject(bucket, filePath);
  }
}
