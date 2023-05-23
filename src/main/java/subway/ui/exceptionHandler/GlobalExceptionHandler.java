package subway.ui.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentException(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> sqlException() {
        return ResponseEntity.badRequest().body("데이터를 처리할 수 없습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> globalException() {
        return ResponseEntity.badRequest().body("요청을 처리할 수 없습니다.");
    }
}
