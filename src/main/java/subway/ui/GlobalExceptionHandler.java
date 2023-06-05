package subway.ui;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.dto.response.ExceptionResponse;
import subway.exception.DuplicatedException;
import subway.exception.InvalidSectionException;
import subway.exception.NotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentsException(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(List.of(exception.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidArgumentException(final MethodArgumentNotValidException exception) {
        List<String> messages = exception.getFieldErrors()
                                         .stream()
                                         .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                         .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(messages));
    }

    @ExceptionHandler({NotFoundException.class, DuplicatedException.class, InvalidSectionException.class})
    public ResponseEntity<ExceptionResponse> handleRuntimeException(final RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(List.of(exception.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(List.of(exception.getMessage())));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionResponse> handleSQLException(final SQLException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(List.of(exception.getMessage())));
    }
}
