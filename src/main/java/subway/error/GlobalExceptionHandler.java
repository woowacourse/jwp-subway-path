package subway.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.error.exception.ErrorCode;
import subway.error.exception.SubwayException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handlingApplicationException(final SubwayException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        log.error("\nSTATUS : {} \nMESSAGE : {}\n",
                errorCode.getStatus(), errorCode.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse(
                        errorCode.getStatus(),
                        errorCode.getMessage()
                ), HttpStatus.valueOf(errorCode.getStatus())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handlingAnotherException(final RuntimeException exception) {
        log.error("\nSTATUS : {} \nMESSAGE : {}\n", 500, exception.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(500, "알 수 없는 예외가 발생했습니다."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
