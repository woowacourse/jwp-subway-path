package subway.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger("Internal Error: ");

    @ExceptionHandler
    public ResponseEntity<String> handle(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(final MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(final IllegalStateException e) {
        return ResponseEntity.ok(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(final Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body("예상치 못한 에러가 발생했습니다.");
    }
}
