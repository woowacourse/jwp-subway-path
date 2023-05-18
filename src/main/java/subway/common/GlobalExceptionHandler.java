package subway.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.InvalidSubwayPathException;
import subway.exception.NoLineException;
import subway.exception.StationDuplicationNameException;
import subway.exception.StationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({StationNotFoundException.class, StationDuplicationNameException.class})
    public ResponseEntity<String> handleStation(final RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({NoLineException.class})
    public ResponseEntity<String> handleLine(final RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({InvalidSubwayPathException.class})
    public ResponseEntity<String> handlePath(final RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
