package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DuplicateStationInLineException.class,
            InvalidDistanceException.class,
            NameLengthException.class,
            SectionMergeException.class,
            SectionNotFoundException.class,
            StationNotFoundException.class,
            LineNotFoundException.class,
            DuplicateLineNameException.class
    })
    public ResponseEntity<String> handleException(RuntimeException runtimeException) {
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }
}
