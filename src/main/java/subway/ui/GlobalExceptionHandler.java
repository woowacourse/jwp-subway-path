package subway.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.exception.DuplicatedNameException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({StationNotFoundException.class, LineNotFoundException.class})
    public ResponseEntity<String> handleLineNotFoundException(RuntimeException exception) {
        log.error("{}: 잘못된 삭제 요청", exception.getClass());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDuplicatedNameException(final DuplicatedNameException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFound(final EmptyResultDataAccessException exception) {
        log.error("{}: 경로를 찾을 수 없습니다", exception.getClass());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleException(final Exception exception) {
        log.error(exception.getMessage());
        return ResponseEntity.internalServerError().build();
    }
}
