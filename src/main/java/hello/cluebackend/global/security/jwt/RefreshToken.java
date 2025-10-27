package hello.cluebackend.global.security.jwt;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refresh_token", timeToLive = 24 * 60 * 60)
@Getter
@ToString
public class RefreshToken {

    @Id
    private String refreshToken;

    private String username;

    public RefreshToken(String refreshToken, String username) {
        this.refreshToken = refreshToken;
        this.username = username;
    }

    public RefreshToken() {
    }
}
