package subway.exception;

import static org.springframework.http.ResponseEntity.internalServerError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String UNEXPECTED_EXCEPTION_CODE = "9999";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(BaseException.class)
    ResponseEntity<ExceptionResponse> handleException(final BaseException e) {
        final BaseExceptionType type = e.exceptionType();
        log.warn("[ERROR] MESSAGE: {}", type.errorMessage());
        return new ResponseEntity<>(ExceptionResponse.from(e), type.httpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(final Exception e) {
        log.error("[ERROR] 예상치 못한 예외 발생", e);
        return internalServerError()
                .body(ExceptionResponse.internalServerError(UNEXPECTED_EXCEPTION_CODE));
    }
}
