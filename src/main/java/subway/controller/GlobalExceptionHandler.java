package subway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ExceptionResponse;
import subway.exeption.SubwayException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse("알 수 없는 오류가 발생했습니다. 잠시 후에 다시 시도해주세요."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse("잘못된 인자가 전달되었습니다."));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> handleSubwayException(final SubwayException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }
}
