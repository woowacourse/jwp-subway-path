package subway.controller;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import subway.exception.SubwayException;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception exception) {
        final String message = exception.getMessage();
        LOGGER.error(message);
        return ResponseEntity.internalServerError().body("알 수 없는 서버 에러가 발생했습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(final MethodArgumentNotValidException exception) {
        final String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
        LOGGER.warn(message);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleException(final MethodArgumentTypeMismatchException exception) {
        final String message = exception.getMessage();
        LOGGER.warn(message);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleException(final MissingServletRequestParameterException exception) {
        final String message = exception.getMessage();
        LOGGER.warn(message);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<String> handleException(final SubwayException exception) {
        final String message = exception.getMessage();
        LOGGER.warn(message);
        return ResponseEntity.badRequest().body(message);
    }
}
