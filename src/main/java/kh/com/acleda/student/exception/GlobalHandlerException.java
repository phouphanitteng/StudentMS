package kh.com.acleda.student.exception;

import kh.com.acleda.student.payload.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<?>> runTimeEx(RuntimeException ex) {
        log.error("Runtime Exception: " , ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("500");
        response.setErrorMessage(ex.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Response<?>> userNotFound(UsernameNotFoundException ex) {

        log.error("UsernameNotFoundException Exception: ", ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("400");
        response.setErrorMessage(ex.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> exception(Exception ex) {
        log.error("Exception: ", ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("500");
        response.setErrorMessage(ex.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<?>> missingReqFieldEx(MethodArgumentNotValidException ex) {
        log.error("Exception: ", ex);
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().stream().findFirst().ifPresent(error -> {
            errors.put("message", error.getDefaultMessage());
        });

        Response<Map<String, String>> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("400");
        response.setErrorMessage(errors.get("message"));
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
