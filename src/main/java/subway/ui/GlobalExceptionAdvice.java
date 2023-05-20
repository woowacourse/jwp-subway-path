package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.AlreadyExistLineException;
import subway.exception.AlreadyExistStationException;
import subway.exception.NoSuchLineException;
import subway.exception.NoSuchShortestPathException;
import subway.exception.NoSuchStationException;
import subway.exception.NotInitializedLineException;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleAnotherException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler({NoSuchLineException.class, NoSuchStationException.class, AlreadyExistLineException.class, AlreadyExistStationException.class, IllegalArgumentException.class, NotInitializedLineException.class, NoSuchShortestPathException.class})
    public ResponseEntity<String> handleInputException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
