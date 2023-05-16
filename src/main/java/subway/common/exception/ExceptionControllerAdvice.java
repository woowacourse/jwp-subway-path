package subway.common.exception;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.internalServerError;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String UNEXPECTED_EXCEPTION_CODE = "9999";
    private static final String REQUEST_FIELD_ERROR = "19999";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(BaseException.class)
    ResponseEntity<ExceptionResponse> handleException(final BaseException e) {
        final BaseExceptionType type = e.exceptionType();
        log.warn("[ERROR] MESSAGE: {}", type.errorMessage());
        return new ResponseEntity<>(ExceptionResponse.from(e), type.httpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatus status,
                                                                  final WebRequest request) {
        final String errorMessage = ex.getFieldErrors().stream()
                .map(it -> it.getField() + " : " + it.getDefaultMessage())
                .collect(Collectors.joining("\n"));
        log.warn("[ERROR] 잘못된 요청이 들어왔습니다: {}", errorMessage);
        return badRequest().body(
                new ExceptionResponse(REQUEST_FIELD_ERROR, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(final Exception e) {
        log.error("[ERROR] 예상치 못한 예외 발생", e);
        return internalServerError()
                .body(ExceptionResponse.internalServerError(UNEXPECTED_EXCEPTION_CODE));
    }
}
