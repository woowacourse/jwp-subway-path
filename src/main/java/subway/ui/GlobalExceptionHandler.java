package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.StationNotExistException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StationNotExistException.class)
    public ResponseEntity<String> stationNotExist(StationNotExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorCode.STATION_NOT_EXIST.getMessage());
    }
}
