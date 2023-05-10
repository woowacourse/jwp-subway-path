package subway.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {


    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(final IllegalArgumentException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({NotFoundStationException.class, NotFoundLineException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final RuntimeException e) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({DuplicateStationException.class, DuplicateLineException.class})
    public ResponseEntity<ExceptionResponse> handleDuplicateException(final RuntimeException e) {
        return ResponseEntity.status(CONFLICT)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(final Exception e) {
        log.error("Ex : ", e);
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(e.getMessage()));
    }

    public static class ExceptionResponse {
        private final String message;

        public ExceptionResponse(final String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
