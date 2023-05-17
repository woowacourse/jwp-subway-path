package subway.controller;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.controller.exception.LineException;
import subway.controller.exception.SectionException;
import subway.controller.exception.StationException;
import subway.controller.exception.SubwayException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StationException.class)
    public ResponseEntity<String> handleStationException(final StationException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(SectionException.class)
    public ResponseEntity<String> handleSectionException(final SectionException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(LineException.class)
    public ResponseEntity<String> handleLineException(final LineException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<String> handleSubwayException(final SubwayException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(final IllegalArgumentException error) {
        return ResponseEntity.internalServerError().body("서버에 일시적인 문제가 생겼습니다. 관리자에게 문의하세요.");
    }
}
