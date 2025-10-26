package hello.cluebackend.common.utils;

import hello.cluebackend.domain.user.model.UserEntity;

public class ClassCodeUtils {

  private ClassCodeUtils() {}

  public static String[] classCodeConverter(UserEntity user) {
    int code = user.getClassCode();
    String grade = String.valueOf(code / 1000);
    String classNumber = String.valueOf((code % 1000) / 100);
    return new String[]{grade, classNumber};
  }
}