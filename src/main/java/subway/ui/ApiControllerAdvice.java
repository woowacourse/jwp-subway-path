package subway.ui;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.exception.DuplicatedLineNameException;
import subway.exception.DuplicatedStationNameException;
import subway.exception.InvalidDistanceException;
import subway.exception.LineNotFoundException;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationNotFoundException;
import subway.ui.dto.ErrorResponse;

@RestControllerAdvice(
        assignableTypes = {LineController.class, StationController.class, PathController.class}
)
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            DuplicatedStationNameException.class,
            DuplicatedLineNameException.class,
            InvalidDistanceException.class,
            StationAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        final String message = exception.getMessage();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(value = {
            LineNotFoundException.class,
            StationNotFoundException.class
    })
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String message = ex.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(message));
    }
}
