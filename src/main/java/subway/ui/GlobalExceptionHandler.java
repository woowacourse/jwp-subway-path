package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.dto.response.ExceptionResponse;
import subway.exception.DuplicatedException;
import subway.exception.InvalidSectionException;
import subway.exception.NotFoundException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentsException(final IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(List.of(e.getMessage())));
    }

    @ExceptionHandler({NotFoundException.class, DuplicatedException.class, InvalidSectionException.class})
    public ResponseEntity<ExceptionResponse> handleRuntimeException(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(List.of(e.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(List.of(e.getMessage())));
    }
}
