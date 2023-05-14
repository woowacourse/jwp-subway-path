package subway.ui;

import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.application.line.port.in.LineNotFoundException;
import subway.exception.BusinessException;
import subway.ui.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    private ResponseEntity<ErrorResponse<String>> handleException(final Exception exception) {
        log.error("예상치 못한 예외가 발생했습니다.", exception);
        return ResponseEntity.internalServerError().body(new ErrorResponse<>("예상치 못한 예외가 발생했습니다."));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse<String>> handleIllegalArgumentException(
        final IllegalArgumentException exception) {
        log.warn("잘못된 인자가 들어왔습니다", exception);
        return ResponseEntity.badRequest().body(new ErrorResponse<>(exception.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        log.warn("유효성 검사에 실패했습니다.", ex);
        final Map<String, String> body = ex.getFieldErrors()
            .stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity.badRequest().body(new ErrorResponse<>(body));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse<String>> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
        log.warn("잘못된 인자가 들어왔습니다.", exception);
        return ResponseEntity.badRequest().body(new ErrorResponse<>(exception.getMessage()));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse<String>> handleBusinessException(final BusinessException exception) {
        log.warn("비즈니스 예외가 발생했습니다.", exception);
        return ResponseEntity.badRequest().body(new ErrorResponse<>(exception.getMessage()));
    }

    @ExceptionHandler
    private ResponseEntity<Void> handleLineNotFound(final LineNotFoundException exception) {
        log.warn("노선을 찾을 수 없습니다.", exception);
        return ResponseEntity.notFound().build();
    }
}
