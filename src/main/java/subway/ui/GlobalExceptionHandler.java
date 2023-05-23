package subway.ui;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.application.exception.SubwayInternalServerException;
import subway.application.exception.SubwayServiceException;
import subway.ui.dto.ErrorResponse;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SubwayServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(final Exception exception) {
        System.out.println(exception.getMessage()); // log 대신 출력
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindingException(final BindException exception) {
        Map<String, String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        System.out.println(errors); // log 대신 출력
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({DataAccessException.class, IllegalArgumentException.class, SubwayInternalServerException.class})
    public ResponseEntity<ErrorResponse> handleInternalServerException(final Exception exception) {
        System.out.println(exception.getMessage()); // log 대신 출력
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
    }
}
