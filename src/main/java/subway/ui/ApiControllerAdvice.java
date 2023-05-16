package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.exception.DuplicatedLineNameException;
import subway.exception.DuplicatedStationNameException;
import subway.exception.InvalidDistanceException;
import subway.exception.LineNotFoundException;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationNotFoundException;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            DuplicatedStationNameException.class,
            DuplicatedLineNameException.class,
            InvalidDistanceException.class,
            StationAlreadyExistsException.class
    })
    public ResponseEntity<Void> handleBadRequest() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = {
            LineNotFoundException.class,
            StationNotFoundException.class
    })
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.notFound().build();
    }


}
