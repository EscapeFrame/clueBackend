package hello.cluebackend.domain.classroom.controller;

import hello.cluebackend.domain.classroom.controller.dto.ClassRoomAllInfoDto;
import hello.cluebackend.domain.classroom.controller.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.controller.dto.ClassRoomDto;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.global.utils.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassRoomController {
    private final JWTUtil jwtUtil;
    private final ClassRoomService classRoomService;

    @GetMapping
    public ResponseEntity<List<ClassRoomCardDto>> getAllClassRooms(HttpServletRequest request){
        String token = jwtUtil.getToken(request);
        UUID userId = jwtUtil.getUserId(token);
        return ResponseEntity.ok(classRoomService.findMyClassRoomById(userId));
    }

    @GetMapping("/{classId}/all")
    public ResponseEntity<ClassRoomAllInfoDto> getAllInfo(@PathVariable UUID classId){
        return ResponseEntity.ok(classRoomService.getAllInfo(classId));
    }

    @PostMapping
    public ResponseEntity<HashMap<?,?>> createClassRoom(@RequestBody ClassRoomDto classRoomDTO, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);
        UUID userId = jwtUtil.getUserId(token);
        if(role != Role.TEACHER) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            classRoomService.createClassRoom(classRoomDTO, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{code}/members")
    public ResponseEntity<?> joinClassRoom(@PathVariable String code, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        UUID userId = jwtUtil.getUserId(token);

        try {
            classRoomService.joinClassRoom(userId, code);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ClassRoomDto> findClassRoom(@PathVariable UUID classId, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);

//        if(role != Role.TEACHER) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

        ClassRoomDto findClassRoomDto = classRoomService.findById(classId);
        return ResponseEntity.ok(findClassRoomDto);
    }

    @PatchMapping("/{classId}")
    public ResponseEntity<?> updateClassRoom(@PathVariable UUID classId, @RequestBody ClassRoomDto classRoomDTO, HttpServletRequest request) {
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