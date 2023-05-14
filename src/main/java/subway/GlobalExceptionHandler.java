package subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.*;
import subway.ui.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<String> stationNotExist(StationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorCode.STATION_NOT_EXIST.getMessage());
    }

    @ExceptionHandler(InvalidSectionLengthException.class)
    public ResponseEntity<String> stationNotExist(InvalidSectionLengthException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorCode.INVALID_SECTION_LENGTH.getMessage());
    }

    @ExceptionHandler(EndStationNotExistException.class)
    public ResponseEntity<String> endStationNotFound(EndStationNotExistException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorCode.NOT_FOUND_END_STATION.getMessage());
    }

    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<String> sectionNotFound(SectionNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorCode.NOT_FOUND_SECTION.getMessage());
    }
}
