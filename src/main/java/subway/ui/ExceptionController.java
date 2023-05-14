package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.exception.LineNotFoundException;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleLineNotFoundException(final LineNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }
}
