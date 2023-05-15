package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.presentation.dto.response.ExceptionResponse;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception ex) {
        logger.error("Exception : ", ex);

        return ResponseEntity.internalServerError().body(new ExceptionResponse("예상치 못한 예외가 발생했습니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(final IllegalArgumentException ex) {
        logger.error("IllegalArgumentException : ", ex);

        return ResponseEntity.badRequest().body(new ExceptionResponse(ex.getMessage()));
    }
}
