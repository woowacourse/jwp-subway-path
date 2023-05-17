package subway.ui.exceptionHandler;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handleRuntimeException(final Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({DataAccessException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Void> handleSQLException(final Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(final Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleAll(final Exception e) {
        e.printStackTrace();
    }
}
