package hello.cluebackend.domain.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import hello.cluebackend.domain.file.exception.S3FileNotFoundException;
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

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  // 파일 추가
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
      return storedFileName;
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드에 실패했습니다.", e);
    }
  }

  // 파일 삭제
  public void deleteFile(String storedFileName) {
    try{
      amazonS3Client.deleteObject(bucket, storedFileName);
    }catch(Exception e){
      throw new RuntimeException("파일 삭제에 실패했습니다.");
    }
  }

  // 파일 다운로드
  public Resource downloadFile(String filePath) throws IOException {
    try{
      S3Object s3Object = amazonS3Client.getObject(bucket, filePath);
      return new InputStreamResource(s3Object.getObjectContent());
    } catch (Exception e) {
      throw new S3FileNotFoundException("해당 파일 경로를 찾을수 없습니다: " + filePath);
    }
  }
}
