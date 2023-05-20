package subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.custom.LineDoesNotContainStationException;
import subway.exception.custom.LineNotExistException;
import subway.exception.custom.StartStationNotExistException;
import subway.exception.custom.StationNotExistException;

@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handle(final Exception exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.internalServerError().build();
    }

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

    @ExceptionHandler(StartStationNotExistException.class)
    public ResponseEntity<ExceptionResponse> handle(final StartStationNotExistException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(LineNotExistException.class)
    public ResponseEntity<ExceptionResponse> handle(final LineNotExistException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(LineDoesNotContainStationException.class)
    public ResponseEntity<ExceptionResponse> handle(final LineDoesNotContainStationException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Void> handle(DuplicateKeyException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
