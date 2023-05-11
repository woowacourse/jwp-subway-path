package subway.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import subway.dto.exception.ExceptionResponse;
import subway.exception.DistanceInvalidException;
import subway.exception.SectionInvalidException;
import subway.exception.SectionNotFoundException;
import subway.exception.UpStationNotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DistanceInvalidException.class)
    public ResponseEntity<ExceptionResponse> distanceInvalidExceptionHandler(final DistanceInvalidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(SectionInvalidException.class)
    public ResponseEntity<ExceptionResponse> sectionInvalidExceptionHandler(final SectionInvalidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException exception) {
        String message = exception.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> sectionNotFoundExceptionHandler(final SectionNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(UpStationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> upStationNotFoundExceptionHandler(final UpStationNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> internalServerErrorHandler(final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
