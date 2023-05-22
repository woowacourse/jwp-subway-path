package subway.controller;

import static java.util.stream.Collectors.joining;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.CustomException;

@RestControllerAdvice
public class ControllerAdvice {

    private static final String UNEXPECTED_ERROR_LOG_FORMAT = "예상치 못한 에러 발생 : " + System.lineSeparator() + "{}";
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleException(final CustomException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> sendErrorMessage(final MethodArgumentNotValidException exception) {
        final String errorMessages = exception.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining(System.lineSeparator()));
        return ResponseEntity.badRequest().body(errorMessages);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleUnexpectedException(final RuntimeException exception) {
        LOGGER.error(UNEXPECTED_ERROR_LOG_FORMAT, convertToString(exception));
        return ResponseEntity.internalServerError().build();
    }

    private String convertToString(final Exception e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
