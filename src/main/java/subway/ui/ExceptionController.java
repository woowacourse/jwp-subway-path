package subway.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.ui.dto.response.ExceptionResponse;

import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionController {

    private final static String LOG_FORMAT = "Class : {}, Message : {}";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleSQLException(final SQLException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity.badRequest().body(new ExceptionResponse("이미 존재하는 값입니다."));
    }
}
