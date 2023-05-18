package subway.adapter.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        logger.warn("[IllegalArgumentException] ", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        logger.warn("[MethodArgumentNotValidException] ", exception);

        List<FieldError> fieldErrors = exception.getFieldErrors();

        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            stringBuilder.append(fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(stringBuilder.toString());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> unhandledException(Exception exception) {
        logger.error("[Internal Server Error] ", exception);

        return ResponseEntity.internalServerError()
                .body("Internal Server Error.");
    }
}
