package hello.cluebackend.global.common.resolver;

import hello.cluebackend.global.common.annotation.CurrentUser;
import hello.cluebackend.global.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final JWTUtil jwtUtil;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUser.class)
            && parameter.getParameterType().equals(UUID.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter,
                                ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    String token = jwtUtil.getToken(request);
    return jwtUtil.getUserId(token);
  }
}
