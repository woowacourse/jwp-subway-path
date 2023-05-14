package subway2.ui;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway2.application.NotFoundException;
import subway2.dto.ExceptionResponse;
import subway2.exception.InvalidSectionException;
import subway2.exception.LineDuplicatedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentsException(final IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(List.of(e.getMessage())));
    }

    @ExceptionHandler({NotFoundException.class, LineDuplicatedException.class, InvalidSectionException.class})
    public ResponseEntity<ExceptionResponse> handleRuntimeException(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(List.of(e.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(List.of(e.getMessage())));
    }

}
