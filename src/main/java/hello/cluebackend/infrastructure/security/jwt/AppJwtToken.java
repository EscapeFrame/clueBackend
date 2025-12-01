package hello.cluebackend.infrastructure.security.jwt;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppJwtToken {

    private String accessToken;
    private String refreshToken;
}
