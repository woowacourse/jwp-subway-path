package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.line.LineException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(LineException.class)
    public ResponseEntity<String> handleLineException(LineException exception) {
        String errorMessage = exception.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
