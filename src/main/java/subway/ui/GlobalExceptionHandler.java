package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.DuplicateLineNameException;
import subway.exception.DuplicateStationInLineException;
import subway.exception.IllegalDestinationException;
import subway.exception.InvalidDistanceException;
import subway.exception.LineNotFoundException;
import subway.exception.NameLengthException;
import subway.exception.SectionMergeException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;
import subway.exception.StationsNotConnectedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DuplicateStationInLineException.class,
            DuplicateLineNameException.class,
            IllegalDestinationException.class,
            InvalidDistanceException.class,
            LineNotFoundException.class,
            NameLengthException.class,
            SectionMergeException.class,
            SectionNotFoundException.class,
            StationNotFoundException.class,
            StationsNotConnectedException.class
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
