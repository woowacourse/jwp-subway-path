package subway.ui.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.SubwayGlobalException;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(SubwayGlobalException.class)
    public ResponseEntity<String> handleGlobalException(SubwayGlobalException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}
