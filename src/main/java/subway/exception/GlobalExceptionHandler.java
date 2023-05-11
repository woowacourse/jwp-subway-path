package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        System.out.println(e.getMessage());
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage());

        return ResponseEntity.badRequest().body(exceptionMessage);
    }
}
