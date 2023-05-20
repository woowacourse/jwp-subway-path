package subway.exception;

import static subway.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static subway.exception.ErrorCode.INVALID_REQUEST;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> globalException(final BadRequestException e) {
        log.error(e.getMessage(), e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorResponse = new ErrorResponse(errorCode, List.of(errorCode.getMessage()));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        final List<String> errorMessage = getErrorMessage(e);
        final ErrorResponse errorResponse = new ErrorResponse(INVALID_REQUEST, errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(final NotFoundException e) {
        log.error(e.getMessage(), e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorResponse = new ErrorResponse(errorCode, List.of(e.getErrorMessage()));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DBException.class)
    public ResponseEntity<ErrorResponse> dbException(final DBException e) {
        log.error(e.getMessage(), e);
        final ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR, List.of(e.getMessage()));
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(final Exception e) {
        log.error(e.getMessage(), e);
        final ErrorCode errorCode = INTERNAL_SERVER_ERROR;
        final ErrorResponse errorResponse = new ErrorResponse(errorCode, List.of(errorCode.getMessage()));
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    private List<String> getErrorMessage(final MethodArgumentNotValidException e) {
        return e.getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
    }
}
