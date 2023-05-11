package subway.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.IllegalDistanceException;
import subway.exception.IllegalSectionException;
import subway.exception.StationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.internalServerError().body("서버에 오류가 발생했습니다.");
    }

    @ExceptionHandler(IllegalSectionException.class)
    public ResponseEntity<String> handleIllegalSectionException(IllegalSectionException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalDistanceException.class)
    public ResponseEntity<String> handleIllegalDistanceSectionException(IllegalDistanceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<String> handleStationNotFoundException(StationNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
