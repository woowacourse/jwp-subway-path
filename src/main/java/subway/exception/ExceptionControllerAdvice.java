package subway.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
                .body(new ExceptionResponse("5000", e.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    ResponseEntity<ExceptionResponse> handleException(BaseException e) {
        final BaseExceptionType type = e.exceptionType();
        log.warn("[ERROR] MESSAGE: {}", type.errorMessage());
        return new ResponseEntity<>(
                new ExceptionResponse(String.valueOf(type.errorCode()), type.errorMessage()),
                type.httpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(final Exception e) {
        log.error("Ex : ", e);
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse("9999", e.getMessage()));
    }
}
