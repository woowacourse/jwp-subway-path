package subway.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedStationNameException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicatedStationNameException(DuplicatedStationNameException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleStationNotFoundException(StationNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedLineNameException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicatedLineNameException(DuplicatedLineNameException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleLineNotFoundException(LineNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LineOrStationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleLineOrStationNotFoundException(LineOrStationNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedSectionException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicatedSectionException(DuplicatedSectionException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleSectionNotFoundException(SectionNotFoundException e) {
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
