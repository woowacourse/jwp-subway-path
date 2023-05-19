package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(final BusinessException exception) {
        final ExceptionResponse response = ExceptionResponse.of(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
