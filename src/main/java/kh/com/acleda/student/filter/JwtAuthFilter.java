package kh.com.acleda.student.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.com.acleda.student.dto.Response;
import kh.com.acleda.student.service.JwtService;
import kh.com.acleda.student.utils.CommonUtils;
import kh.com.acleda.student.utils.ConstantVariable;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger LOG = LogManager.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {

        final String authHeader = request.getHeader("Authorization");
        LOG.debug("header: " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if(!checkWhitelistedPaths(request)){
                generateForUnauthorized(response);
            }
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String email = this.jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (email != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if (this.jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            LOG.error("Exception: " + exception);
            throw new RuntimeException(exception.getMessage());
        }
    }

    private void generateForUnauthorized(HttpServletResponse response) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Response<?> responseBody = new Response<>();
        responseBody.setResponseCode(1);
        responseBody.setErrorCode("401");
        responseBody.setErrorMessage(ConstantVariable.TOKEN_ERR);
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