package hello.cluebackend.domain.directory.presentation;

import hello.cluebackend.domain.directory.presentation.dto.RequestDirectoryDto;
import hello.cluebackend.domain.directory.service.DirectoryService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/api/directory")
public class DirectoryController {

    private final DirectoryService directoryService;
    private final JWTUtil jwtUtil;

    public DirectoryController(DirectoryService directoryService, JWTUtil jwtUtil) {
        this.directoryService = directoryService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createDirectory(@RequestBody RequestDirectoryDto requestDirectoryDto, HttpServletRequest request){
        String token = jwtUtil.getToken(request);
        if(!jwtUtil.getRole(token).equals(Role.TEACHER)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            directoryService.createDirectory(requestDirectoryDto);
        } catch (IllegalArgumentException e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updateDirectory(@RequestBody RequestDirectoryDto requestDirectoryDto, HttpServletRequest request){
        String token = jwtUtil.getToken(request);
        if(!jwtUtil.getRole(token).equals(Role.TEACHER)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            directoryService.updateDirectory(requestDirectoryDto);
        } catch (IllegalArgumentException e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDirectory(@RequestBody RequestDirectoryDto requestDirectoryDto, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        Role role = jwtUtil.getRole(token);
        if(!role.equals(Role.TEACHER)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            directoryService.deleteById(requestDirectoryDto.getDirectoryId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
