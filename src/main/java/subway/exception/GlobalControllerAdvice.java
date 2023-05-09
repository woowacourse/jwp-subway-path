package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
