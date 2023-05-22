package subway.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import subway.dto.response.Response;
import subway.exception.line.DuplicateLineException;
import subway.exception.station.DuplicateStationException;
import subway.exception.section.IllegalDistanceException;
import subway.exception.path.IllegalPathException;
import subway.exception.section.IllegalSectionException;
import subway.exception.line.LineNotFoundException;
import subway.exception.station.StationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String BAD_REQUEST_MESSAGE = "잘못된 요청입니다.";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRuntimeException(RuntimeException e) {
        log.info(e.getMessage(), e);
        return Response.internalServerError()
                .message("서버에 오류가 발생했습니다.")
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.badRequest()
                .message(BAD_REQUEST_MESSAGE)
                .validation(e)
                .build();
    }

    @ExceptionHandler(IllegalSectionException.class)
    public ResponseEntity<Response> handleIllegalSectionException(IllegalSectionException e) {
        return Response.badRequest()
                .message(BAD_REQUEST_MESSAGE)
                .validation("upBoundStationName", e.getMessage())
                .validation("downBoundStationName", e.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalDistanceException.class)
    public ResponseEntity<Response> handleIllegalDistanceSectionException(IllegalDistanceException e) {
        return Response.badRequest()
                .message(BAD_REQUEST_MESSAGE)
                .validation("distance", e.getMessage())
                .build();
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<Response> handleStationNotFoundException(StationNotFoundException e) {
        return Response.badRequest()
                .message(BAD_REQUEST_MESSAGE)
                .validation("stationName", e.getMessage())
                .build();
    }

    @ExceptionHandler(DuplicateStationException.class)
    public ResponseEntity<Response> handleDuplicateStationException(DuplicateStationException e) {
        return Response.badRequest()
                .message(BAD_REQUEST_MESSAGE)
                .validation("stationName", e.getMessage())
                .build();
    }

    @ExceptionHandler(DuplicateLineException.class)
    public ResponseEntity<Response> handleDuplicateLineException(DuplicateLineException e) {
        return Response.badRequest()
                .message(BAD_REQUEST_MESSAGE)
                .validation("lineName", e.getMessage())
                .build();
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<Response> handleLineNotFoundException(LineNotFoundException e) {
        return Response.badRequest()
                .message(BAD_REQUEST_MESSAGE)
                .validation("lineId", e.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalPathException.class)
    public ResponseEntity<Response> handleIllegalPathException(IllegalPathException e) {
        return Response.badRequest()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return Response.badRequest()
                .message(BAD_REQUEST_MESSAGE)
                .validation(e.getName(), e.getMostSpecificCause().getMessage())
                .build();
    }
}
