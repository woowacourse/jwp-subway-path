package subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.custom.StationNotExistException;

@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handle(final IllegalArgumentException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(StationNotExistException.class)
    public ResponseEntity<ExceptionResponse> handle(final StationNotExistException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }
}
