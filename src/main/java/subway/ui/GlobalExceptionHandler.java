package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleValidationException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("[ERROR] " + exception.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
