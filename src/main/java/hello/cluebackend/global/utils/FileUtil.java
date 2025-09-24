package hello.cluebackend.global.utils;

import java.util.UUID;

public class FileUtil {
  public static String generateUniqueFileName(String originalFileName) {
    if (originalFileName == null || !originalFileName.contains(".")) {
      throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
    }

    String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
    String uuid = UUID.randomUUID().toString();

    return uuid + ext;
  }
}
