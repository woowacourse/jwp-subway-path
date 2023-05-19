package subway.controller;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.controller.dto.ErrorResponse;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        logger.warn(ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({StationNotFoundException.class, LineNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoSuchElementException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Iterator<ConstraintViolation<?>> iterator =
                ex.getConstraintViolations().iterator();

        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            ConstraintViolation<?> constraintViolation = iterator.next();
            stringBuilder.append(constraintViolation.getMessage());
        }
        logger.warn(stringBuilder.toString());
        return ResponseEntity.badRequest().body(new ErrorResponse(stringBuilder.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse(ex.getMessage()));
    }
}
