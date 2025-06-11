package kh.com.acleda.student.exception;

import kh.com.acleda.student.dto.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    private static final Logger log = LogManager.getLogger(GlobalHandlerException.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<?>> runTimeEx(RuntimeException ex) {

        log.error("Runtime Exception: " + ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("500");
        response.setErrorMessage(ex.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> exception(Exception ex) {
        log.error("Exception: " + ex);
        Response<?> response = new Response<>();
        response.setResponseCode(1);
        response.setErrorCode("500");
        response.setErrorMessage(ex.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
