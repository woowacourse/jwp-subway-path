package subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.response.ErrorResponse;
import subway.exception.business.*;
import subway.ui.ErrorCode;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSectionLengthException.class)
    public ResponseEntity<ErrorResponse> stationNotExist(InvalidSectionLengthException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_SECTION_LENGTH.getMessage());

        return ResponseEntity.status(ErrorCode.INVALID_SECTION_LENGTH.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<ErrorResponse> sectionNotFound(SectionNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_FOUND_SECTION.getMessage());

        return ResponseEntity.status(ErrorCode.NOT_FOUND_SECTION.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidSectionConnectException.class)
    public ResponseEntity<ErrorResponse> sectionNotFound(InvalidSectionLengthException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_SECTION_CONNECTION.getMessage());

        return ResponseEntity.status(ErrorCode.INVALID_SECTION_CONNECTION.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorResponse> sectionNotFound(StationNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.STATION_NOT_EXIST.getMessage());

        return ResponseEntity.status(ErrorCode.STATION_NOT_EXIST.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(AlreadyExistStationException.class)
    public ResponseEntity<ErrorResponse> sectionNotFound(AlreadyExistStationException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.STATION_ALREADY_EXIST.getMessage());

        return ResponseEntity.status(ErrorCode.STATION_ALREADY_EXIST.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handleSubwayException(SubwayException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

}
