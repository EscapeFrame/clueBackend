package hello.cluebackend.domain.classroom.presentation;

import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDTO;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/class")
public class ClassRoomController {
    private final JWTUtil jwtUtil;
    private final ClassRoomService classRoomService;

    public ClassRoomController(JWTUtil jwtUtil, ClassRoomService classRoomService) {
        this.jwtUtil = jwtUtil;
        this.classRoomService = classRoomService;
    }

    @GetMapping("/request")
    public List<ClassRoomDTO> getClassRoom(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtUtil.getUsername(token);

        // 수정 필요
        return classRoomService.findMyClassRoomById(1L);
    }

    @PostMapping("/create-room")
    public ResponseEntity<HashMap<?,?>> createClassRoom(@RequestBody ClassRoomDTO classRoomDTO, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Role role = jwtUtil.getRole(token);
        Long userId = jwtUtil.getUserId(token);
        if(!role.name().equals("teacher")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        classRoomService.createClassRoom(classRoomDTO, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
