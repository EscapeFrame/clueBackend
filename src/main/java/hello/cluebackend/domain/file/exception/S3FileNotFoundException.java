package hello.cluebackend.domain.file.exception;

import com.amazonaws.services.s3.model.AmazonS3Exception;

public class S3FileNotFoundException extends AmazonS3Exception {
  public S3FileNotFoundException(String message) { super(message); }
}