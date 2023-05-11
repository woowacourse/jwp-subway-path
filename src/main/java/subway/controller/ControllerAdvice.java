package subway.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.response.ErrorResponse;
import subway.exception.ApiIllegalArgumentException;
import subway.exception.ApiNoSuchResourceException;

@RestControllerAdvice
public class ControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ApiIllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handleApiIllegalArgumentException(ApiIllegalArgumentException exception) {
        logger.warn("[ApiIllegalArgumentException]", exception);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleInvalidArgument(MethodArgumentNotValidException exception) {
        logger.warn("[MethodArgumentNotValidException] ", exception);

        List<FieldError> fieldErrors = exception.getFieldErrors();

        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            stringBuilder.append(fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(stringBuilder.toString()));
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
