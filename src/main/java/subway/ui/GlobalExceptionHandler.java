package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.exception.*;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({DuplicateSectionNameException.class, DuplicateSectionAddException.class, DuplicateSectionException.class,
    DuplicateSectionNameException.class,DuplicateLineNameException.class})
    public ResponseEntity<String> handleDuplicationException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler({InValidDistanceException.class, InValidLineNameException.class, InvalidSectionException.class,
            InValidStationNameException.class})
    public ResponseEntity<String> handleInvalidException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler({NotConnectSectionException.class, NotExistSectionException.class})
    public ResponseEntity<String> handle(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleAnyException(Exception e) {
        return ResponseEntity.internalServerError().body("예기치 못한 에러가 발생했습니다.");
    }
}
