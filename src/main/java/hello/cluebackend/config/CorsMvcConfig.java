package hello.cluebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

  @Value("${front.base-url}")
  private String frontBaseUrl;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 모든 API 경로 허용
            .allowedOriginPatterns(frontBaseUrl)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // 허용할 HTTP 메서드
            .allowedHeaders("*") // 모든 요청 헤더 허용
            .exposedHeaders("Authorization", "Set-Cookie") // 클라이언트가 접근 가능한 헤더
            .allowCredentials(true); // 쿠키, 인증 정보 허용
  }
}