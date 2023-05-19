package subway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ExceptionResponse;
import subway.exeption.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse("알 수 없는 오류가 발생했습니다. 잠시 후에 다시 시도해주세요."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse("잘못된 인자가 전달되었습니다."));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> handleSubwayException(final SubwayException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidDistanceException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDistanceException(final InvalidDistanceException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidLineException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidLineException(final InvalidLineException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPathException(final InvalidPathException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidStationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidStationException(final InvalidStationException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleLineNotFoundException(final LineNotFoundException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(NOT_FOUND)
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleStationNotFoundException(final StationNotFoundException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(NOT_FOUND)
                .body(new ExceptionResponse(exception.getMessage()));
    }
}
