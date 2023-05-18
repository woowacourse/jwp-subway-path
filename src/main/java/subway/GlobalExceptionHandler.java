package subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.response.ErrorResponse;
import subway.exception.*;
import subway.ui.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> sectionNotFound(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorResponse> stationNotExist(StationNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.STATION_NOT_EXIST.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidSectionLengthException.class)
    public ResponseEntity<ErrorResponse> stationNotExist(InvalidSectionLengthException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_SECTION_LENGTH.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(EndStationNotExistException.class)
    public ResponseEntity<ErrorResponse> endStationNotFound(EndStationNotExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_FOUND_END_STATION.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<ErrorResponse> sectionNotFound(SectionNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_FOUND_SECTION.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidSectionConnectException.class)
    public ResponseEntity<ErrorResponse> sectionNotFound(InvalidSectionLengthException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_SECTION_CONNECTION.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
