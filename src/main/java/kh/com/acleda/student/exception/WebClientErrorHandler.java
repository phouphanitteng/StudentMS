package kh.com.acleda.student.exception;

import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import kh.com.acleda.student.payload.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.netty.http.client.PrematureCloseException;

import java.util.concurrent.TimeoutException;

@Slf4j
@RestControllerAdvice
public class WebClientErrorHandler {

    @ExceptionHandler(ReadTimeoutException.class)
    public ResponseEntity<Response<?>> handleReadTimeoutException(ReadTimeoutException ex) {
        log.error("Read timeout occurred", ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("408");
        response.setErrorMessage("Read timeout occurred");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(WriteTimeoutException.class)
    public ResponseEntity<Response<?>> handleWriteTimeoutException(WriteTimeoutException ex) {
        log.error("Write timeout occurred", ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("408");
        response.setErrorMessage("Write timeout occurred");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<Response<?>> handleTimeoutException(TimeoutException ex) {
        log.error("Timeout occurred", ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("408");
        response.setErrorMessage("Timeout occurred");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(PrematureCloseException.class)
    public ResponseEntity<Response<?>> handlePrematureCloseException(PrematureCloseException ex) {
        log.error("Connection closed prematurely", ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("502");
        response.setErrorMessage("Connection closed prematurely");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<Response<?>> handleWebClientException(WebClientException ex) {
        log.error("WebClient error occurred: {}, cause: {}", ex.getMessage(),
                ex.getCause() != null ? ex.getCause().getClass().getSimpleName() : "none", ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("503");
        response.setErrorMessage("WebClient error: " + ex.getMessage());
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

}