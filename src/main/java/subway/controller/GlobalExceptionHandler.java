package subway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.controller.exception.OptionalHasNoLineException;
import subway.controller.exception.OptionalHasNoStationException;
import subway.dto.response.ErrorResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        List<ErrorResponse> responses = exception.getBindingResult().getAllErrors()
                .stream()
                .map(error -> new ErrorResponse(
                        ((FieldError) error).getField() + " : " + error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(responses);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException(SQLException exception) {
        log.error("Error from handleSQLException", exception);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({OptionalHasNoLineException.class, OptionalHasNoStationException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
}
