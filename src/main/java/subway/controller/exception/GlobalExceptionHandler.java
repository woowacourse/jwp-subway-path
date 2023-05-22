package subway.controller.exception;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(error.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException error) {
        final Map<String, String> errorMessageByFields = error.getBindingResult()
                .getAllErrors()
                .stream()
                .map(FieldError.class::cast)
                .collect(Collectors.toUnmodifiableMap(FieldError::getField, ObjectError::getDefaultMessage));

        return ResponseEntity.badRequest().body(errorMessageByFields);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception error) {
        return ResponseEntity.internalServerError().body("서버에 일시적인 문제가 생겼습니다. 관리자에게 문의하세요.");
    }
}
