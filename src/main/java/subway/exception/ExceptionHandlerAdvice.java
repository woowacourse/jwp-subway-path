package subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.domain.path.Fee;
import subway.exception.common.NotFoundException;
import subway.exception.distance.DistanceException;
import subway.exception.fee.FeeException;
import subway.exception.input.InputException;
import subway.exception.line.LineException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(LineException.class)
    public ResponseEntity<String> handleLineException(LineException exception) {
        String errorMessage = exception.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        String errorMessage = exception.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(InputException.class)
    public ResponseEntity<String> handleInputException(InputException exception) {
        String errorMessage = exception.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(DistanceException.class)
    public ResponseEntity<String> handleDistanceException(DistanceException exception) {
        String errorMessage = exception.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(FeeException.class)
    public ResponseEntity<String> handleDistanceException(FeeException exception) {
        String errorMessage = exception.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
