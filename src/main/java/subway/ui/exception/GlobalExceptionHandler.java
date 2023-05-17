package subway.ui.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnExpectedException(Exception exception) {
        final String errorMessage = exception.getMessage();
        log.error("예상치 못한 예외가 발생했습니다 : {}", errorMessage);

        return ResponseEntity.internalServerError().body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> ha가ndleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("잘못된 입력 정보 입니다 : {}", exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
