package kh.com.acleda.student.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;


@Configuration
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws IOException, ServletException {

        try {

            MDC.put("traceId", UUID.randomUUID().toString());
            MDC.put("method", request.getMethod());
            MDC.put("uri", request.getRequestURI());
            MDC.put("ip", request.getRemoteAddr());

            String userId = request.getHeader("userId");
            MDC.put("logRouter", userId != null ? userId : "anonymous");

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

}