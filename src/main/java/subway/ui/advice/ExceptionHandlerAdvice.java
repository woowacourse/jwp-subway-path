package subway.ui.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ExceptionResponse;
import subway.exception.*;
import subway.ui.LineController;
import subway.ui.LineStationController;
import subway.ui.PathController;
import subway.ui.StationController;

@RestControllerAdvice(
        assignableTypes = {
                StationController.class,
                LineController.class,
                LineStationController.class,
                PathController.class
        })
public class ExceptionHandlerAdvice {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalInputForDomainException.class,
            DuplicatedDataException.class,
            DataConstraintViolationException.class,
            NotFoundDataException.class
    })
    public ResponseEntity<ExceptionResponse> badRequestExceptionHandle(final Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptionResponse);
    }

    @ExceptionHandler(UnsupportedParameterException.class)
    public ResponseEntity<ExceptionResponse> unsupportedExceptionHandle(final Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> unexpectedExceptionHandle(final Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                status.value(),
                status.getReasonPhrase(),
                "예기치 못한 에러입니다."
        );

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptionResponse);
    }

}
