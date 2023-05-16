package subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ExceptionResponse;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(LineNotFoundException.class)
    ResponseEntity<ExceptionResponse> handleLineException(final LineNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(LineAlreadyExistException.class)
    ResponseEntity<ExceptionResponse> handleLineException(final LineAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(StationNotFoundException.class)
    ResponseEntity<ExceptionResponse> handleStationException(final StationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(StationAlreadyExistException.class)
    ResponseEntity<ExceptionResponse> handleStationException(final StationAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ExceptionResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ExceptionResponse> handleIllegalStateException(final IllegalStateException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

}


