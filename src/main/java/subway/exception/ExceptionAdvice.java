package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ExceptionResponse;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(LineException.class)
    ResponseEntity<ExceptionResponse> handleLineException(final LineException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(StationException.class)
    ResponseEntity<ExceptionResponse> handleStationException(final StationException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ExceptionResponse> handleStationException(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }


}

