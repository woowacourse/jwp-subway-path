package subway.advice;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ExceptionResponse;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger;
    
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException (final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }
}
