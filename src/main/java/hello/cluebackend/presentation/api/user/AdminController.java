package hello.cluebackend.presentation.api.user;

import hello.cluebackend.application.user.dto.request.UpdateRoleUserDto;
import hello.cluebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<Void> updateRole(@RequestBody UpdateRoleUserDto updateRoleUserDto) {
        userService.updateRole(updateRoleUserDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}