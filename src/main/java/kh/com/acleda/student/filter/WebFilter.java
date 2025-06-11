package kh.com.acleda.student.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Set;

@Component
@jakarta.servlet.annotation.WebFilter("/*")
public class WebFilter implements Filter {

    private static final Logger log = LogManager.getLogger(WebFilter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectWriter prettyPrinter = objectMapper.writerWithDefaultPrettyPrinter();
    private static final Set<String> SENSITIVE_KEYS = Set.of("otp","password", "confirmPassword", "oldPassword", "newPassword");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(wrappedRequest, wrappedResponse);

        logRequestDetails(wrappedRequest);
        logResponseDetails(wrappedResponse);

        wrappedResponse.copyBodyToResponse();
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) {
        StringBuilder msg = new StringBuilder();

        msg.append("REQUEST ")
                .append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI());

        // Headers
        msg.append("\nRequest Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            msg.append("\n  ").append(name).append(": ").append(request.getHeader(name));
        }
        // Body
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            msg.append("\nRequest Body:\n").append(maskSensitiveFields(body));
        }

        log.info(msg.toString());
    }

    private void logResponseDetails(ContentCachingResponseWrapper response) {
        StringBuilder msg = new StringBuilder();

        msg.append("RESPONSE ")
                .append(response.getStatus());

        // Headers
        msg.append("\nResponse Headers:");
        for (String name : response.getHeaderNames()) {
            msg.append("\n  ").append(name).append(": ").append(response.getHeader(name));
        }

        // Body
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            msg.append("\nResponse Body:\n").append(maskSensitiveFields(body));
        }

        log.info(msg.toString());
    }
    private String maskSensitiveFields(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            maskJsonNode(root);
            return prettyPrinter.writeValueAsString(root);
        } catch (Exception e) {
            // If body is not valid JSON, return it as-is
            return body;
        }
    }

    private void maskJsonNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objNode = (ObjectNode) node;
            objNode.fieldNames().forEachRemaining(field -> {
                if (SENSITIVE_KEYS.contains(field)) {
                    objNode.put(field, "****");
                } else {
                    maskJsonNode(objNode.get(field));
                }
            });
        } else if (node.isArray()) {
            for (JsonNode arrayElement : node) {
                maskJsonNode(arrayElement);
            }
        }
    }
}
