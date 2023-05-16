package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.controller.exception.LineException;
import subway.controller.exception.SectionException;
import subway.controller.exception.StationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StationException.class)
    public ResponseEntity<String> handleStationException(final StationException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(SectionException.class)
    public ResponseEntity<String> handleSectionException(final SectionException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(LineException.class)
    public ResponseEntity<String> handleLineException(final LineException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(final IllegalStateException error) {
        return ResponseEntity.internalServerError().body("서버에 일시적인 문제가 생겼습니다. 관리자에게 문의하세요.");
    }
}
