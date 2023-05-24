package subway.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.controller.dto.response.ExceptionResponse;
import subway.exception.DuplicatedLineNameException;
import subway.exception.DuplicatedSectionException;
import subway.exception.DuplicatedStationNameException;
import subway.exception.InvalidDirectionException;
import subway.exception.InvalidDistanceException;
import subway.exception.LineNotFoundException;
import subway.exception.LineOrStationNotFoundException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DuplicatedStationNameException.class, DuplicatedLineNameException.class, DuplicatedSectionException.class})
    public ResponseEntity<ExceptionResponse> handleDuplicatedException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({StationNotFoundException.class, LineNotFoundException.class,
            LineOrStationNotFoundException.class, SectionNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDistanceException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDistanceException(InvalidDistanceException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidDirectionException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDirectionException(InvalidDirectionException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        try {
            final String errorMessage = Objects.requireNonNull(e.getRootCause()).getMessage();
            return ResponseEntity.badRequest().body(new ExceptionResponse(errorMessage));
        } catch (NullPointerException ex) {
            return handleRuntimeException();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final String errorMessage = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" "));
        return ResponseEntity.badRequest().body(new ExceptionResponse(errorMessage));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException() {
        return ResponseEntity.internalServerError().body(new ExceptionResponse("서버 내부 오류입니다."));
    }
}
