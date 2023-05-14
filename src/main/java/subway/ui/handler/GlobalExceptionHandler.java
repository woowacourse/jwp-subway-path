package subway.ui.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException(SQLException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(IllegalArgumentException e) {
        log.error("Error from IllegalArgumentException = ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
