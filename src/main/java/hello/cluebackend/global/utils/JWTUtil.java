package hello.cluebackend.global.utils;

import hello.cluebackend.domain.user.domain.Role;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public UUID getUserId(String token) {
        String uuid = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", String.class);
        return UUID.fromString(uuid);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public Role getRole(String token) {
        String roleString = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);

        return Role.valueOf(roleString);  // String → Enum
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String createJwt(String category, UUID userId, String username, String role, Long expiredMs) {
        return  Jwts.builder()
                .claim("category", category)
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String getToken(HttpServletRequest request) {
        String authHeader =  request.getHeader("Authorization");
        return authHeader.replace("Bearer ", "");
    }

    public Map<String, String> getClaims(String token) {
        Map<String, String> claims = new HashMap<>();
        claims.put("category", getCategory(token));
        claims.put("username", getUsername(token));
        claims.put("userId", getUserId(token).toString());
        claims.put("role", getRole(token).name());
        return claims;
    }
}
