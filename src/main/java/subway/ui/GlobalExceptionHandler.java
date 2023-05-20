package subway.ui;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.exception.DuplicatedNameException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({StationNotFoundException.class, LineNotFoundException.class})
    public ResponseEntity<String> handleLineNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDuplicatedNameException(final DuplicatedNameException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFound(final EmptyResultDataAccessException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleException(final Exception exception) {
        return ResponseEntity.internalServerError().build();
    }
}
