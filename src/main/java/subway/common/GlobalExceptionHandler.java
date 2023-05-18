package subway.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.StationDuplicationNameExcpetion;
import subway.exception.StationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({StationNotFoundException.class, StationDuplicationNameExcpetion.class})
    public ResponseEntity<String> handle(final RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
