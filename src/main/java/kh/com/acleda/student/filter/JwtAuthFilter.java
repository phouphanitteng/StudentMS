package kh.com.acleda.student.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.entity.Student;
import kh.com.acleda.student.repository.StudentRepository;
import kh.com.acleda.student.service.JwtService;
import kh.com.acleda.student.utils.CommonUtils;
import kh.com.acleda.student.constant.ConstantVariable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final StudentRepository studentRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {

        final String authHeader = request.getHeader("Authorization");
        log.debug("request header : " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if(!checkWhitelistedPaths(request)){
                generateForUnauthorized(response, ConstantVariable.INVALID_HEADER);
            }
            filterChain.doFilter(request, response);
            return;
        }

        try {

            final String jwt = authHeader.substring(7);
            final String email = this.jwtService.extractUsername(jwt);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (email != null && authentication == null) {
                Student student = studentRepository.findByIdEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Student not found with email: " + email));
                if (this.jwtService.isTokenValid(jwt, student)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            student,
                            null,
                            student.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            log.error("JWT expired: {}", ex.getMessage(), ex);
            generateForUnauthorized(response, ConstantVariable.JWT_EXPIRED);
        } catch (MalformedJwtException ex) {
            log.error("Malformed JWT: {}", ex.getMessage(), ex);
            generateForUnauthorized(response, ConstantVariable.JWT_INVALID);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage(), ex);
            generateForUnauthorized(response, ConstantVariable.JWT_INVALID);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT: {}", ex.getMessage(), ex);
            generateForUnauthorized(response, ConstantVariable.JWT_UNSUPPORT);
        } catch (JwtException ex) {
            log.error("JWT error: {}", ex.getMessage(), ex);
            generateForUnauthorized(response, "JWT error: " + ex.getMessage());
        } catch (Exception exception) {
            log.error("Exception: ", exception);
            generateForUnauthorized(response, exception.getMessage());
        }
    }

    private void generateForUnauthorized(HttpServletResponse response, String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Response<?> responseBody = new Response<>();
        responseBody.setResponseCode(1);
        responseBody.setErrorCode("401");
        responseBody.setErrorMessage(!StringUtils.isNotBlank(message) ? ConstantVariable.TOKEN_ERR : message);
        responseBody.setData(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    private boolean checkWhitelistedPaths(HttpServletRequest request) {
        String whitelistPath = CommonUtils.getCommonProperties("security.whitelisted.paths");

        if (whitelistPath != null && !whitelistPath.isEmpty()) {
            for (String list : whitelistPath.split(",")) {
                list = list.trim();
                if (request.getServletPath().startsWith(list.replace("*", ""))) {
                    return true;
                }
            }
        }
        return false;
    }

}