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

    @GetMapping
    public ResponseEntity<List<ClassRoomDTO>> getAllClassRooms(HttpServletRequest request){
        String token = jwtUtil.getToken(request);
        Long userId = jwtUtil.getUserId(token);
        return ResponseEntity.ok(classRoomService.findMyClassRoomById(userId));
    }

    @PostMapping
    public ResponseEntity<HashMap<?,?>> createClassRoom(@RequestBody ClassRoomDTO classRoomDTO, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);
        Long userId = jwtUtil.getUserId(token);
        if(!role.name().equals("teacher")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            classRoomService.createClassRoom(classRoomDTO, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{classid}")
    public ClassRoomDTO getClassRoom(@PathVariable Long classid, HttpServletRequest request) {
        return classRoomService.getClassRoomByClassId(classid);
    }

}