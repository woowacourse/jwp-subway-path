package subway.ui.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.application.exception.InvalidUserInputException;

import java.sql.SQLException;

@RestControllerAdvice
public class CommonControllerAdvice {
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidUserInputException.class)
    public ResponseEntity<String> handleInvalidUserInputException(InvalidUserInputException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
