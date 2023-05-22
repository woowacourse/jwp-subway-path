package subway.controller.advice;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.exception.ExceptionResponse;
import subway.exception.invalid.InvalidException;
import subway.exception.notfound.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ExceptionResponse> invalidExceptionHandler(final InvalidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundExceptionHandler(final NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.from(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException e) {
        String message = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> internalServerErrorHandler(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.from(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
