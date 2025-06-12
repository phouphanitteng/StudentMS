package kh.com.acleda.student.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import kh.com.acleda.student.payload.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Response<?>> handleExpiredJwtException(ExpiredJwtException ex) {
        log.error("JWT expired: {}", ex.getMessage(), ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("401");
        response.setErrorMessage("JWT token has expired");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Response<?>> handleMalformedJwtException(MalformedJwtException ex) {
        log.error("Malformed JWT: {}", ex.getMessage(), ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("400");
        response.setErrorMessage("Invalid JWT token: Malformed structure");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Response<?>> handleSignatureException(SignatureException ex) {
        log.error("Invalid JWT signature: {}", ex.getMessage(), ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("401");
        response.setErrorMessage("Invalid JWT token: Signature verification failed");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<Response<?>> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        log.error("Unsupported JWT: {}", ex.getMessage(), ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("400");
        response.setErrorMessage("Unsupported JWT token");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Response<?>> handleJwtException(JwtException ex) {
        log.error("JWT error: {}", ex.getMessage(), ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("400");
        response.setErrorMessage("JWT error: " + ex.getMessage());
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}