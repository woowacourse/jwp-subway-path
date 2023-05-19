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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception exception) {
        System.out.println(exception.getMessage());
        return ResponseEntity.internalServerError().body("서버 내부에서 문제가 발생했습니다.");
    }
}
