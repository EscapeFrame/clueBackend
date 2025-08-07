//package hello.cluebackend.global.common.response;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//public enum ResponseCode {
//  //== 2xx: 성공 ==//
//  SUCCESS(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
//  CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),
//  DELETED(HttpStatus.OK, "리소스가 성공적으로 삭제되었습니다."),
//
//  //== 4xx: 클라이언트 오류 ==//
//  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
//  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
//  FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
//  NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
//  CONFLICT(HttpStatus.CONFLICT, "요청 충돌이 발생했습니다."),
//
//  //== 5xx: 서버 오류 ==//
//  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
//  SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서비스를 이용할 수 없습니다.");
//
//  private final HttpStatus httpStatus;
//  private final String message;
//
//  ResponseCode(HttpStatus httpStatus, String message){
//    this.httpStatus = httpStatus;
//    this.message = message;
//  }
//
//  public HttpStatus getHttpStatus() {
//    return httpStatus;
//  }
//
//  public String getMessage() {
//    return message;
//  }
//
//  public int getStatusCode() {
//    return httpStatus.value();
//  }
//
//  public static <T> ResponseEntity<ApiResponse<T>> buildResponse(ResponseCode code, T data){
//    return ResponseEntity.status(code.getHttpStatus()).body(ApiResponse.success(code, data));
//  }
//}
