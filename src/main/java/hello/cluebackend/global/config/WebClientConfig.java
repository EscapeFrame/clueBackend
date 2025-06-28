package hello.cluebackend.global.config;

import org.springframework.context.annotation.Configuration;

package hello.cluebackend.global.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
            .baseUrl("https://api.example.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)) // 2MB
            .build();
  }

  // 타임아웃과 커넥션 풀 설정이 필요한 경우
  @Bean
  public WebClient advancedWebClient() {
    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 연결 타임아웃
            .responseTimeout(Duration.ofSeconds(30)) // 응답 타임아웃
            .doOnConnected(conn -> conn
                    .addHandlerLast(new ReadTimeoutHandler(30)) // 읽기 타임아웃
                    .addHandlerLast(new WriteTimeoutHandler(30))); // 쓰기 타임아웃

    return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl("https://api.example.com")
            .build();
  }
}