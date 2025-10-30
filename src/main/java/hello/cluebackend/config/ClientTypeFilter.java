package hello.cluebackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ClientTypeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/oauth2/authorization/google")) {
            String clientType = request.getParameter("client_type");

            if (clientType != null && clientType.equals("web") || clientType.equals("app")) {
                HttpSession session = request.getSession();
                session.setAttribute("client_type", clientType);
                log.info("client type is " + clientType);
            }
        }

        filterChain.doFilter(request, response);
    }
}
