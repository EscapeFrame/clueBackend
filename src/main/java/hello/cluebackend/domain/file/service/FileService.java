package hello.cluebackend.domain.file.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private static final int DEFAULT_EXPIRATION_MINUTES = 60;

  public String storeFile(MultipartFile file) throws IOException {
    String originalFilename = file.getOriginalFilename();
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    String storedFileName = UUID.randomUUID().toString() + extension;

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    amazonS3Client.putObject(new PutObjectRequest(bucket, storedFileName, file.getInputStream(), metadata));
    return storedFileName;
  }

  public void deleteFile(String storedFileName) {
    amazonS3Client.deleteObject(bucket, storedFileName);
  }

  @Deprecated
  public Resource downloadFile(String filePath) {
    S3Object s3Object = amazonS3Client.getObject(bucket, filePath);
    return new InputStreamResource(s3Object.getObjectContent());
  }

  public String getPresignedDownloadUrl(String storedFileName) {
    return getPresignedDownloadUrl(storedFileName, DEFAULT_EXPIRATION_MINUTES);
  }

  public String getPresignedDownloadUrl(String storedFileName, int expirationMinutes) {
    Date expiration = new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000);
    return amazonS3Client.generatePresignedUrl(bucket, storedFileName, expiration, HttpMethod.GET).toString();
  }
}
