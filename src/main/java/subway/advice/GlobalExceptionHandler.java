package subway.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.exception.ExceptionResponse;
import subway.exception.*;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DistanceForkedException.class)
    public ResponseEntity<ExceptionResponse> distanceInvalidExceptionHandler(final DistanceForkedException exception) {
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

    @ExceptionHandler(SectionDuplicatedException.class)
    public ResponseEntity<ExceptionResponse> sectionDuplicatedExceptionHandler(final SectionDuplicatedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(SectionSeparatedException.class)
    public ResponseEntity<ExceptionResponse> sectionSeparatedExceptionHandler(final SectionSeparatedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(NameIsBlankException.class)
    public ResponseEntity<ExceptionResponse> nameIsBlankExceptionHandler(final NameIsBlankException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(DistanceValueInvalidException.class)
    public ResponseEntity<ExceptionResponse> distanceValueInvalidExceptionHandler(final DistanceValueInvalidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(LineNumberUnderMinimumNumber.class)
    public ResponseEntity<ExceptionResponse> lineNumberUnderMinimumNumberHandler(final LineNumberUnderMinimumNumber exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
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

    @ExceptionHandler(ColorNotBlankException.class)
    public ResponseEntity<ExceptionResponse> colorNotBlankExceptionHandler(final ColorNotBlankException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(LineNotMatchedException.class)
    public ResponseEntity<ExceptionResponse> lineNotMatchedExceptionHandler(final LineNotMatchedException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> internalServerErrorHandler(final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
