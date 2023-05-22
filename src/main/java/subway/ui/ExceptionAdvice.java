package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.exception.InvalidInputException;

@ControllerAdvice
public class ExceptionAdvice {
    
    @ExceptionHandler({InvalidInputException.class})
    public ResponseEntity<String> handleInvalidInputException(final InvalidInputException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        final StringBuilder stringBuilder = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            stringBuilder.append("[");
            stringBuilder.append(error.getCode());
            stringBuilder.append("] ");
            stringBuilder.append(error.getObjectName());
            stringBuilder.append(" ");
            stringBuilder.append(error.getField());
            stringBuilder.append(" ");
            stringBuilder.append(error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(stringBuilder.toString());
    }
}
