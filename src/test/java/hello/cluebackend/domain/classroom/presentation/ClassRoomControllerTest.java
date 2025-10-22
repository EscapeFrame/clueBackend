package hello.cluebackend.domain.classroom.presentation;

import hello.cluebackend.domain.classroom.controller.ClassRoomController;
import hello.cluebackend.domain.classroom.controller.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.global.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassRoomControllerTest {

    @InjectMocks
    private ClassRoomController classRoomController;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private ClassRoomService classRoomService;

    @Test
    void testGetAllClassRooms() {
        // given
        String token = "fake-token";
        UUID userId = UUID.randomUUID();

        UUID classRoomId1 = UUID.randomUUID();
        UUID classRoomId2 = UUID.randomUUID();

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(jwtUtil.getToken(mockRequest)).thenReturn(token);
        when(jwtUtil.getUserId(token)).thenReturn(userId);

        List<ClassRoomCardDto> mockList = List.of(
                ClassRoomCardDto.builder()
                        .classRoomId(classRoomId1)
                        .name("자바를 자바라")
                        .sort("JAVA")
                        .target("2-2")
                        .studentCount(2)
                        .build(),
                ClassRoomCardDto.builder()
                        .classRoomId(classRoomId2)
                        .name("자바를 자바라")
                        .sort("JAVA")
                        .target("2-1")
                        .studentCount(2)
                        .build()
        );
        when(classRoomService.findMyClassRoomById(userId)).thenReturn(mockList);
        // when
        ResponseEntity<List<ClassRoomCardDto>> response = classRoomController.getAllClassRooms(mockRequest);

        // then.
        assertThat(200).isEqualTo(response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertThat(2).isEqualTo(response.getBody().size());
        assertThat("자바를 자바라").isEqualTo(response.getBody().get(0).getName());
    }
}