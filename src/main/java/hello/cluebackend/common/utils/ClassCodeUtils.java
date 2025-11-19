package hello.cluebackend.common.utils;

import hello.cluebackend.domain.user.model.UserEntity;

public class ClassCodeUtils {

  private ClassCodeUtils() {}

  public static String[] classCodeConverter(UserEntity user) {
    String grade = String.valueOf(user.getGrade());
    String classNumber = String.valueOf(user.getClassNo());
    return new String[]{grade, classNumber};
  }
}