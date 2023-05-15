package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.ui.dto.ExceptionResponse;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException exception) {
        return ResponseEntity.internalServerError().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(Exception exception) {
        String message = "서버에 예기치 못한 에러가 발생했습니다.";
        return ResponseEntity.internalServerError().body(new ExceptionResponse(message));
    }
}
