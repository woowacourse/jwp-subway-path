package subway.adapter.in.web.exception;

import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.common.exception.SubwayNoSuchResourceException;

@RestControllerAdvice
public class ExceptionAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(SubwayIllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handleApiIllegalArgumentException(SubwayIllegalArgumentException exception) {
        logger.warn("[SubwayIllegalArgumentException]", exception);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleInvalidArgument(MethodArgumentNotValidException exception) {
        logger.warn("[MethodArgumentNotValidException]", exception);

        List<FieldError> fieldErrors = exception.getFieldErrors();

        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            stringBuilder.append(fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(stringBuilder.toString()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    private ResponseEntity<ErrorResponse> handleIncorrectRequestException(Exception exception) {
        logger.warn("[IncorrectRequestException]", exception);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse("요청 값이 잘못되었습니다."));
    }

    @ExceptionHandler(SubwayNoSuchResourceException.class)
    private ResponseEntity<ErrorResponse> handleNoSuchResourceException(SubwayNoSuchResourceException exception) {
        logger.warn("[SubwayNoSuchResourceException]", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponse> handleSQLException(RuntimeException exception) {
        logger.warn("[SQLException]", exception);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Bad Request."));
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> unhandledException(Exception exception) {
        logger.error("[Internal Server Error]", exception);

        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Internal Server Error."));
    }
}
