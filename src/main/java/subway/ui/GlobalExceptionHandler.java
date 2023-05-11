package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.ui.dto.exception.ExceptionDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handle(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e.getMessage()));
    }
}
