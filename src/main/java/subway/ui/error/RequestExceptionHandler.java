package subway.ui.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.GlobalException;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<String> handleGlobalException(GlobalException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}