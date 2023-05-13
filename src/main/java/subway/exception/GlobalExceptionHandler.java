package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DuplicateStationInLineException.class,
            InvalidDistanceException.class,
            NameLengthException.class,
            SectionNotFoundException.class,
            StationNotFoundException.class,
            LineNotFoundException.class,
            DuplicateLineNameException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<String> handleException(RuntimeException runtimeException) {
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }
}
