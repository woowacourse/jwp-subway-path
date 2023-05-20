package subway.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.exception.ExceptionResponse;
import subway.exception.ColorNotBlankException;
import subway.exception.InvalidDistanceException;
import subway.exception.LineNotFoundException;
import subway.exception.LineNumberUnderMinimumNumber;
import subway.exception.LinesEmptyException;
import subway.exception.NameIsBlankException;
import subway.exception.SectionDuplicatedException;
import subway.exception.SectionForkedException;
import subway.exception.SectionNotConnectException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;
import subway.exception.UpStationNotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException exception) {
        String message = exception.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(message, HttpStatus.BAD_REQUEST.value()));
    }

    private static ResponseEntity<ExceptionResponse> getResponseOfBadRequest(final RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(SectionForkedException.class)
    public ResponseEntity<ExceptionResponse> sectionForkedExceptionHandler(final SectionForkedException exception) {
        return getResponseOfBadRequest(exception);
    }

    @ExceptionHandler(SectionDuplicatedException.class)
    public ResponseEntity<ExceptionResponse> sectionDuplicatedExceptionHandler(final SectionDuplicatedException exception) {
        return getResponseOfBadRequest(exception);
    }

    @ExceptionHandler(NameIsBlankException.class)
    public ResponseEntity<ExceptionResponse> nameIsBlankExceptionHandler(final NameIsBlankException exception) {
        return getResponseOfBadRequest(exception);
    }

    @ExceptionHandler(InvalidDistanceException.class)
    public ResponseEntity<ExceptionResponse> invalidDistanceExceptionHandler(final InvalidDistanceException exception) {
        return getResponseOfBadRequest(exception);
    }

    @ExceptionHandler(LineNumberUnderMinimumNumber.class)
    public ResponseEntity<ExceptionResponse> lineNumberUnderMinimumNumberHandler(final LineNumberUnderMinimumNumber exception) {
        return getResponseOfBadRequest(exception);
    }

    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> sectionNotFoundExceptionHandler(final SectionNotFoundException exception) {
        return getResponseOfNotFound(exception);
    }

    private static ResponseEntity<ExceptionResponse> getResponseOfNotFound(final RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.from(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(UpStationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> upStationNotFoundExceptionHandler(final UpStationNotFoundException exception) {
        return getResponseOfNotFound(exception);
    }

    @ExceptionHandler(ColorNotBlankException.class)
    public ResponseEntity<ExceptionResponse> colorNotBlankExceptionHandler(final ColorNotBlankException exception) {
        return getResponseOfBadRequest(exception);
    }

    @ExceptionHandler(SectionNotConnectException.class)
    public ResponseEntity<ExceptionResponse> sectionNotConnectExceptionHandler(final SectionNotConnectException exception) {
        return getResponseOfBadRequest(exception);
    }

    @ExceptionHandler(LinesEmptyException.class)
    public ResponseEntity<ExceptionResponse> linesEmptyExceptionHandler(final LinesEmptyException exception) {
        return getResponseOfBadRequest(exception);
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<ExceptionResponse> lineNotFoundExceptionHandler(final LineNotFoundException exception) {
        return getResponseOfNotFound(exception);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> stationNotFoundExceptionHandler(final StationNotFoundException exception) {
        return getResponseOfNotFound(exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> internalServerErrorHandler(final Exception exception) {
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logger.error("Server error : " + exception.getMessage());

        String internalServerErrorMessage = "내부 서버의 문제입니다. 관리자에게 문의해주세요.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.from(internalServerErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
