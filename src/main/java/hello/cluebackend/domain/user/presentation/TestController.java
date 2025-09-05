package hello.cluebackend.domain.user.presentation;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import hello.cluebackend.global.config.JWTUtil;
import hello.cluebackend.global.security.jwt.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final JWTUtil jwtUtil;

    @PostMapping("/test")
    public ResponseEntity<EntityModel<String>> issueToken(@RequestParam UUID userId, @RequestParam String username, @RequestParam String role, HttpServletResponse response) {
        String access = jwtUtil.createJwt("access", userId, username, role, 100 * 60 * 60 * 1000L);

        EntityModel<String> entityModel = EntityModel.of("JWT access token and refresh token issued for dev use.");

        Link selfLink = linkTo(TestController.class).slash("test").withSelfRel();
        entityModel.add(selfLink);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + access)
                .body(entityModel);
    }
}