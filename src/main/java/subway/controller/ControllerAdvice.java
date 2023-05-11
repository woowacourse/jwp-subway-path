package subway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.response.ErrorResponse;
import subway.exception.ApiIllegalArgumentException;
import subway.exception.ApiNoSuchResourceException;

@RestController
public class ControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ApiIllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handleApiIllegalArgumentException(ApiIllegalArgumentException exception) {
        logger.warn("[ApiIllegalArgumentException]", exception);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(ApiNoSuchResourceException.class)
    private ResponseEntity<ErrorResponse> handleNoSuchResourceException(ApiNoSuchResourceException exception) {
        logger.warn("[ApiNoSuchResourceException]", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> unhandledException(Exception exception) {
        logger.error("[Internal Server Error] ", exception);

        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Internal Server Error."));
    }
}
