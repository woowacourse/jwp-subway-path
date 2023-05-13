package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import subway.ui.dto.response.ExceptionResponse;

import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(final IllegalArgumentException ex) {
        logger.error("IllegalArgumentException : ", ex);

        return ResponseEntity.badRequest().body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler({SQLException.class, DuplicateKeyException.class})
    public ResponseEntity<ExceptionResponse> handleDuplicateKeyException(final Exception ex) {
        logger.error("SQLException : ", ex);

        return ResponseEntity.badRequest().body(new ExceptionResponse("이미 존재하는 값입니다."));
    }
}
