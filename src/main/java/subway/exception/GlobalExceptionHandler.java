package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleException(Exception e) {
        System.out.println(e.getMessage());
        ExceptionMessage exceptionMessage = new ExceptionMessage("현재 서버가 응답할 수 없습니다.");

        return ResponseEntity.internalServerError().body(exceptionMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleIllegalArgumentException(IllegalArgumentException e) {
        System.out.println(e.getMessage());
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage());

        return ResponseEntity.badRequest().body(exceptionMessage);
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotExistException(NotExistException e) {
        System.out.println(e.getMessage());

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleIllegalStateException(IllegalStateException e) {
        System.out.println(e.getMessage());
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage());

        return ResponseEntity.badRequest().body(exceptionMessage);
    }
}
