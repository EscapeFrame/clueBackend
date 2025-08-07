//package hello.cluebackend.global.common.response;
//
//import hello.cluebackend.assignment.domain.Assignment;
//import lombok.*;
//
//import java.util.List;
//
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class ApiResponse<T> {
//
//  private static final String SUCCESS_STATUS = "success";
//  private static final String FAIL_STATUS = "fail";
//  private static final String ERROR_STATUS = "error";
//
//  private int code;
//  private String message;
//  private T data;
//
//  public ApiResponse(int code, String message, T data){
//    this.code = code;
//    this.message = message;
//    this.data = data;
//  }
//
//  public static <T> ApiResponse<T> success(T data){
//    return new ApiResponse<>(, "요청 성공", data);
//  }
//
//  public static <T> ApiResponse<T> success(ResponseCode code){
//    return new ApiResponse<>(code);
//  }
//
//  public static List<Assignment> error(String message) {
//    return new ApiResponse<>("ERROR", message, null);
//  }
//}
