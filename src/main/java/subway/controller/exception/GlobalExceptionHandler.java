package subway.controller.exception;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidStationException.class)
    public ResponseEntity<String> handleInvalidStationException(final InvalidStationException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(InvalidSectionException.class)
    public ResponseEntity<String> handleInvalidSectionException(final InvalidSectionException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(InvalidLineException.class)
    public ResponseEntity<String> handleInvalidLineException(final InvalidLineException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<String> handleStationNotFoundException(final StationNotFoundException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<String> handleLineNotFoundException(final LineNotFoundException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(final BusinessException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
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
