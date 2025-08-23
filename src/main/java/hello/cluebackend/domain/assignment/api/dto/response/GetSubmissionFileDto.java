package hello.cluebackend.domain.assignment.api.dto.response;

public class GetSubmissionFileDto {
  private Long SubmissionAttachmentId;
  private String fileName; // 만약 파일이 아닌 url인 경우 url 주소
  private int fileSize; // url일 경우 보내지 않음.
}
