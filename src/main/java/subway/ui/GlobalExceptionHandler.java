package subway.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.ui.dto.response.ExceptionResponse;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static String LOG_FORMAT = "Class : {}, Message : {}";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleGlobalException(final Exception e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.from("예기치 못한 오류가 발생했습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from("요청 데이터가 잘못되었습니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(e.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionResponse> handleSQLException(final SQLException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from("허용되지 않는 값을 등록하고 있습니다."));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateKeyException(final DuplicateKeyException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from("이미 등록된 값을 등록하고 있습니다."));
    }
}
