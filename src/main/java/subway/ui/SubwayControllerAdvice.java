package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import subway.dto.ErrorResponse;
import subway.exception.DatabaseException;
import subway.exception.DomainException;

@ControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
        final ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),
                exception.getExceptionType().getReason());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDatabaseException(DatabaseException exception) {
        final ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getExceptionType().getReason()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(RuntimeException exception) {
        final ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
