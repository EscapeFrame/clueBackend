package hello.cluebackend.domain.classroom.presentation;

import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
        Long userId = 1L;

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(jwtUtil.getToken(mockRequest)).thenReturn(token);
        when(jwtUtil.getUserId(token)).thenReturn(userId);

        List<ClassRoomCardDto> mockList = List.of(
                new ClassRoomCardDto(1L, "자바를 자바라", "JAVA", "2-2", 2),
                new ClassRoomCardDto(2L, "자바를 자바라", "JAVA", "2-1", 2)
        );
        when(classRoomService.findMyClassRoomById(userId)).thenReturn(mockList);
        // when
        ResponseEntity<List<ClassRoomCardDto>> response = classRoomController.getAllClassRooms(mockRequest);

        // then
        assertThat(200).isEqualTo(response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertThat(2).isEqualTo(response.getBody().size());
        assertThat("자바를 자바라").isEqualTo(response.getBody().get(0).getName());
    }
}