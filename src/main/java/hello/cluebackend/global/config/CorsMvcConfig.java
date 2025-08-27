package hello.cluebackend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry corsRegistry) {
    corsRegistry.addMapping("/**")
            .allowedOriginPatterns("http://10.150.149.87") // 해당 IP만 허용
            .allowedMethods("*")         // GET, POST, PUT, DELETE 등 모든 메서드 허용
            .allowedHeaders("*")         // 모든 헤더 허용
            .exposedHeaders("Authorization", "Set-Cookie") // 클라이언트에서 읽을 헤더
            .allowCredentials(true);     // 쿠키 및 인증 헤더 허용
  }
}