package hello.cluebackend.domain.classroom.presentation;

import hello.cluebackend.domain.assignment.api.dto.response.GetAllClassRoomAssignmentDto;
import hello.cluebackend.domain.assignment.application.AssignmentCommandService;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDto;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.global.common.annotation.CurrentUser;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassRoomController {
    private final JWTUtil jwtUtil;
    private final ClassRoomService classRoomService;
    private final AssignmentCommandService assignmentCommandService;

    @GetMapping
    public ResponseEntity<List<ClassRoomCardDto>> getAllClassRooms(HttpServletRequest request){
        String token = jwtUtil.getToken(request);
        Long userId = jwtUtil.getUserId(token);
        return ResponseEntity.ok(classRoomService.findMyClassRoomById(userId));
    }

    @GetMapping("/{classId}/all")
    public ResponseEntity<?> getAllInfo(HttpServletRequest request, @PathVariable Long classId){
        String token = jwtUtil.getToken(request);
//        Long userId = jwtUtil.getUserId(token);
//        Role role = jwtUtil.getRole(token);

        return ResponseEntity.ok(classRoomService.getAllInfo(classId));
    }

  // 교실 과제 전체 조회
  @GetMapping("/{classId}/assignments")
  public ResponseEntity<List<GetAllClassRoomAssignmentDto>> getAllClassroomAssignment(@CurrentUser Long userId, @PathVariable Long classId) {
    ClassRoom classRoom = classRoomService.findById(classId).toEntity();
    List<GetAllClassRoomAssignmentDto> result = assignmentCommandService.findAllClassroomAssignment(classRoom);
    return ResponseEntity.ok(result);
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
        } catch (EntityNotFoundException e){
            log.error(e.getMessage());
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

//        if(role != Role.TEACHER) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

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