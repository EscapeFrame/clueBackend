package hello.cluebackend.domain.classroom.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDTO;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;
import hello.cluebackend.domain.user.presentation.dto.UserDTO;
import hello.cluebackend.global.config.JWTUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassRoomController.class)
@Import(ClassRoomControllerTest.MockConfig.class)
class ClassRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ClassRoomService classRoomService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public JWTUtil jwtUtil() {
            return mock(JWTUtil.class);
        }

        @Bean
        public ClassRoomService classRoomService() {
            return mock(ClassRoomService.class);
        }
    }

    @Test
    void getClassRoomList_success() throws Exception {
        Long userId = 1L;
        String token = "faketoken";

        when(jwtUtil.getToken(any())).thenReturn(token);
        when(jwtUtil.getUserId(token)).thenReturn(userId);

        when(classRoomService.findMyClassRoomById(userId)).thenReturn(
                List.of(ClassRoomDTO.builder().classRoomId(1L).name("AI반").build())
        );

        mockMvc.perform(get("/api/class")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].classRoomId").value(1L))
                .andExpect(jsonPath("$[0].name").value("AI반"));
    }
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Configuration
//    static class TestConfig {
//        @Bean
//        public JWTUtil jwtUtil() {
//            return mock(JWTUtil.class);
//        }
//
//        @Bean
//        public ClassRoomService classRoomService() {
//            return mock(ClassRoomService.class);
//        }
//    }
//
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Autowired
//    private ClassRoomService classRoomService;
//
//    @Test
//    @DisplayName("GET /api/class - 성공")
//    void getClassRoomList_success() throws Exception {
//        UserDTO userDTO = new UserDTO(1L, "asdf@gmail.com", Role.STUDENT, "김한결", 2105, "2105김한결");
//        CustomOAuth2User oauth2User = new CustomOAuth2User(userDTO);
//
//        String accessToken = jwtUtil.createJwt("access", oauth2User.getUserId(), oauth2User.getUsername(), Role.STUDENT.name(), 60*60*1000L);
//
//        assertThat(jwtUtil.getUserId(accessToken)).isEqualTo(oauth2User.getUserId());
//        when(classRoomService.findMyClassRoomById(oauth2User.getUserId()))
//                .thenReturn(Collections.singletonList(ClassRoomDTO.builder()
//                        .classRoomId(1L)
//                        .build()));
//
//        mockMvc.perform(get("/api/class")
//                        .header("Authorization", "Bearer " +  accessToken))
//                .andExpect(status().isOk());
//
//        verify(classRoomService).findMyClassRoomById(userId);
//    }
//
//    @Test
//    @DisplayName("POST /api/class/create-room - 성공 (teacher)")
//    void createClassRoom_teacher_success() throws Exception {
//        Long userId = 1L;
//        when(jwtUtil.getUserId("mocktoken")).thenReturn(userId);
//        when(jwtUtil.getRole("mocktoken")).thenReturn(Role.TEACHER);
//
//        ClassRoomDTO classRoomDTO = new ClassRoomDTO();
//        String requestBody = objectMapper.writeValueAsString(classRoomDTO);
//
//        mockMvc.perform(post("/api/class/create-room")
//                        .header("Authorization", "Bearer mocktoken")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isCreated());
//
//        verify(classRoomService).createClassRoom(classRoomDTO, userId);
//    }
//
//    @Test
//    @DisplayName("POST /api/class/create-room - 실패 (학생 권한)")
//    void createClassRoom_student_forbidden() throws Exception {
//        when(jwtUtil.getRole("mocktoken")).thenReturn(Role.STUDENT);
//
//        ClassRoomDTO classRoomDTO = new ClassRoomDTO();
//        String requestBody = objectMapper.writeValueAsString(classRoomDTO);
//
//        mockMvc.perform(post("/api/class/create-room")
//                        .header("Authorization", "Bearer mocktoken")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isForbidden());
//
//        verify(classRoomService, never()).createClassRoom(any(), any());
//    }
//
//    @Test
//    @DisplayName("GET /api/class/{classid} - 성공")
//    void getClassRoomById_success() throws Exception {
//        Long classId = 10L;
//        ClassRoomDTO dto = new ClassRoomDTO();
//        when(classRoomService.getClassRoomByClassId(classId)).thenReturn(dto);
//
//        mockMvc.perform(get("/api/class/{classid}", classId))
//                .andExpect(status().isOk());
//
//        verify(classRoomService).getClassRoomByClassId(classId);
//    }
}