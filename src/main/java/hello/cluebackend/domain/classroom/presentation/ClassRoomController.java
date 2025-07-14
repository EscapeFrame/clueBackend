package hello.cluebackend.domain.classroom.presentation;

import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDto;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.presentation.dto.UserDto;
import hello.cluebackend.domain.user.service.UserService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
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
    public ResponseEntity<List<ClassRoomCardDto>> getAllClassRooms(HttpServletRequest request){
        String token = jwtUtil.getToken(request);
        Long userId = jwtUtil.getUserId(token);
        return ResponseEntity.ok(classRoomService.findMyClassRoomById(userId));
    }

    @PostMapping
    public ResponseEntity<HashMap<?,?>> createClassRoom(@RequestBody ClassRoomDto classRoomDTO, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);
        Long userId = jwtUtil.getUserId(token);
        if(role != Role.TEACHER) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            classRoomService.createClassRoom(classRoomDTO, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{code}/members")
    public ResponseEntity<?> joinClassRoom(@PathVariable String code, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Long userId = jwtUtil.getUserId(token);

        try {
            classRoomService.joinClassRoom(userId, code);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ClassRoomDto> findClassRoom(@PathVariable Long classId, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);

        if(role != Role.TEACHER) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ClassRoomDto findClassRoomDto = classRoomService.findById(classId);
        return ResponseEntity.ok(findClassRoomDto);
    }

    @PatchMapping("/{classId}")
    public ResponseEntity<?> updateClassRoom(@PathVariable Long classId, @RequestBody ClassRoomDto classRoomDTO, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);

        if(role != Role.TEACHER) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            classRoomService.updateClassRoom(classId, classRoomDTO);
        } catch (IllegalArgumentException e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }
}