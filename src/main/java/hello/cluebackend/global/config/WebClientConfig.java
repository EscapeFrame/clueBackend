package hello.cluebackend.global.config;

import org.springframework.context.annotation.Configuration;

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
  private static final int TIMEOUT_MS = 60000;

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
            .baseUrl("https://api.example.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .build();
  }

  @Bean
  public WebClient advancedWebClient() {
    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_MS)
            .doOnConnected(conn -> conn
                    .addHandlerLast(new ReadTimeoutHandler(TIMEOUT_MS))
                    .addHandlerLast(new WriteTimeoutHandler(TIMEOUT_MS)
                    )
            )
            .responseTimeout(Duration.ofSeconds(TIMEOUT_MS));

    return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl("https://api.example.com")
            .build();
  }
}