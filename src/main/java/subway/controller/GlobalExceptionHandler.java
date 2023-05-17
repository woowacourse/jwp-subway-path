package subway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    private ResponseEntity<String> handleException(final Exception exception) {
        log.error("예상치 못한 예외가 발생했습니다.", exception);
        return ResponseEntity.internalServerError().body("예상치 못한 예외가 발생했습니다.");
    }

    @ExceptionHandler
    private ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log.debug("잘못된 인자가 들어왔습니다", exception);
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
