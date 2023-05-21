package subway.exception;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DELIMITER = ", ";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        logger.error("ERROR: ", e);
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse("서버가 응답할 수 없습니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        logger.warn("WARN: ", e);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse("올바르지 않은 요청입니다."));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> handleSubwayException(final SubwayException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleLineNotFoundException(final LineNotFoundException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindException(final BindException e) {
        final String errorMessage = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(DELIMITER));
        logger.warn(errorMessage);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(errorMessage));
    }
}
