package subway.ui.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.BadRequestException;
import subway.exception.GlobalException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {
        log.error("Error from BadRequestException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> globalException(final GlobalException e) {
        log.info("Error from GlobalException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpectedException(final Exception e) {
        log.error("Error from UnExpectedException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse("서버에 예상치 못한 문제가 발생했습니다. 잠시후 다시 시도해주세요");

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
