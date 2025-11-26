package hello.cluebackend.application.user.dto.response;

import hello.cluebackend.domain.user.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "first_user", timeToLive = 5 * 60)
@Getter
@ToString
@Builder
public class UserRedisDto {

    @Id
    private String token;

    private String email;
    private String username;
    private Role role;
}
