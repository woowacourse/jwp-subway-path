package subway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
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
        log.error("Error from illegalArgumentException", exception);
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        log.error("Error from MethodArgumentNotValidException", exception);
        List<ErrorResponse> responses = exception.getBindingResult().getAllErrors()
                .stream()
                .map(error -> new ErrorResponse(
                        ((FieldError) error).getField() + " : " + error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(responses);
    }

    @ExceptionHandler({SQLException.class, DuplicateKeyException.class})
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException exception) {
        log.error("Error from handleSQLException", exception);
        ErrorResponse errorResponse = new ErrorResponse("[ERROR] DB 작업 중 에러가 발생했습니다. 서버 담당자에게 문의 주세요.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({OptionalHasNoLineException.class, OptionalHasNoStationException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(Exception exception) {
        log.error("Error from optionalHasNoValueException", exception);
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnexpectedException(Exception exception) {
        log.error("Error from unexpectedException", exception);
        return ResponseEntity.internalServerError().build();
    }
}
