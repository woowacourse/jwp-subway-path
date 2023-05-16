package subway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        logger.error(exception.getMessage());
        return ResponseEntity
                .internalServerError()
                .body("알 수 없는 오류가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException exception) {
        logger.warn(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
