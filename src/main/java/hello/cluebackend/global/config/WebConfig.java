package hello.cluebackend.global.config;

import hello.cluebackend.global.common.resolver.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final OctetStreamReadMsgConverter octetStreamReadMsgConverter;
  private final CurrentUserArgumentResolver currentUserArgumentResolver;

  public WebConfig(OctetStreamReadMsgConverter octetStreamReadMsgConverter,
                   CurrentUserArgumentResolver currentUserArgumentResolver) {
    this.octetStreamReadMsgConverter = octetStreamReadMsgConverter;
    this.currentUserArgumentResolver = currentUserArgumentResolver;
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(octetStreamReadMsgConverter);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserArgumentResolver);
  }
}