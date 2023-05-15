package subway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.response.ErrorResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(final IllegalStateException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError().build();
    }

}
