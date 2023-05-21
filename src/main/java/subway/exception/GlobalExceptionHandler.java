package subway.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public GlobalExceptionHandler() {
        super();
    }

    private ResponseEntity<Object> errorResponse(final String ex) {
        final ErrorResponse errorResponse = ErrorResponse.from(
                ex,
                String.valueOf(BAD_REQUEST.value()),
                LocalDateTime.now());

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(errorResponse);
    }

    private void logInfo(final String message) {
        logger.info(message);
    }

    private void logError(final String message) {
        logger.error(message);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(final MissingPathVariableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logInfo(ex.getMessage());
        return errorResponse(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logInfo(ex.getMessage());
        return errorResponse(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logInfo(ex.getMessage());
        return errorResponse(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logInfo(ex.getMessage());
        return errorResponse(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logInfo(ex.getMessage());
        return errorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    private ResponseEntity<Object> handleException(final Exception ex) {
        logError(ex.getMessage());
        return errorResponse("예상하지 못한 문제가 발생하였습니다. 관리자에게 문의해주세요.");
    }
}
