package hello.cluebackend.domain.submission.presentation;

import hello.cluebackend.domain.classroomuser.application.ClassroomUserService;
import hello.cluebackend.domain.submission.application.SubmissionCommandService;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import hello.cluebackend.global.common.annotation.CurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubmissionCommandController.class)
class SubmissionCommandControllerTest {

  private static final UUID FIXED_USER_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean private SubmissionCommandService submissionCommandService;
  @MockitoBean private ClassroomUserService classroomUserService;

  @BeforeEach
  void setUp(WebApplicationContext wac) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .setCustomArgumentResolvers(new CurrentUserUuidArgumentResolver(FIXED_USER_ID))
            .build();
  }

  @Test
  @DisplayName("GET /api/submissions/{classId} - 교실 멤버일 때 빈 리스트 200 OK")
  void findSubmission_ok_emptyList() throws Exception {
    UUID classId = UUID.randomUUID();

    when(classroomUserService.isUserInClassroom(eq(classId), eq(FIXED_USER_ID))).thenReturn(true);
    when(submissionCommandService.findAllByAssignmentId(eq(FIXED_USER_ID), eq(classId)))
            .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/submissions/{classId}", classId))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json("[]"));
  }

  @Test
  @DisplayName("GET /api/submissions/{classId} - 교실 멤버가 아닐 때 403 Forbidden")
  void findSubmission_forbidden_whenNotClassMember() throws Exception {
    UUID classId = UUID.randomUUID();

    when(classroomUserService.isUserInClassroom(eq(classId), eq(FIXED_USER_ID))).thenReturn(false);

    mockMvc.perform(get("/api/submissions/{classId}", classId))
            .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("GET /api/submissions/assignment/{submissionId} - 본인 제출물 단건 조회 200 OK (본문 null 허용)")
  void findAllSubmission_ok_nullBodyAllowed() throws Exception {
    UUID submissionId = UUID.randomUUID();

    when(submissionCommandService.findByAssignmentId(eq(FIXED_USER_ID), eq(submissionId)))
            .thenReturn(null);

    MockHttpServletResponse response = mockMvc.perform(get("/api/submissions/assignment/{submissionId}", submissionId))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

    assert response.getContentAsByteArray().length == 0;
  }

  @Test
  @DisplayName("GET /api/submissions/{assignmentId}/check - 제출 여부 리스트 200 OK (빈 리스트)")
  void checkAssignment_ok_emptyList() throws Exception {
    UUID assignmentId = UUID.randomUUID();

    when(submissionCommandService.checkAssignment(eq(FIXED_USER_ID), eq(assignmentId)))
            .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/submissions/{assignmentId}/check", assignmentId))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json("[]"));
  }

  @Test
  @DisplayName("GET /api/submissions/{submissionAttachmentId}/download - 첨부파일 다운로드 200 OK 및 헤더 검증")
  void submissionAttachmentDownload_ok() throws Exception {
    UUID submissionAttachmentId = UUID.randomUUID();

    String originalFileName = "sample.txt";
    String contentType = MediaType.TEXT_PLAIN_VALUE;
    byte[] fileBytes = "hello-file".getBytes(StandardCharsets.UTF_8);
    Resource resource = new ByteArrayResource(fileBytes);

    SubmissionAttachment attachment = Mockito.mock(SubmissionAttachment.class);
    when(attachment.getOriginalFileName()).thenReturn(originalFileName);
    when(attachment.getContentType()).thenReturn(contentType);
    when(attachment.getValue()).thenReturn("/fake/path/sample.txt");

    when(submissionCommandService.findSubmissionAttachmentByIdOrThrow(eq(submissionAttachmentId)))
            .thenReturn(attachment);
    when(submissionCommandService.downloadAttachment(eq(attachment)))
            .thenReturn(resource);

    mockMvc.perform(get("/api/submissions/{submissionAttachmentId}/download", submissionAttachmentId))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", containsString(MediaType.TEXT_PLAIN_VALUE)))
            .andExpect(header().string("Content-Disposition", containsString("attachment")))
            .andExpect(header().string("Content-Disposition", containsString(originalFileName)));
  }

  // @CurrentUser UUID 주입을 위한 간단한 ArgumentResolver
  static class CurrentUserUuidArgumentResolver implements org.springframework.web.method.support.HandlerMethodArgumentResolver {
    private final UUID fixedUserId;

    CurrentUserUuidArgumentResolver(UUID fixedUserId) {
      this.fixedUserId = fixedUserId;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
      return parameter.getParameterAnnotation(CurrentUser.class) != null
              && UUID.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                  org.springframework.web.context.request.NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) {
      return fixedUserId;
    }
  }
}