package subway.ui;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.NoDataFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public ResponseEntity<String> handleNoDataFoundException(final NoDataFoundException exception) {
        log.warn("존재하지 않은 데이터에 접근하였습니다. \n{}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("존재하지 않은 데이터에 접근하였습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentRequest(final IllegalArgumentException exception) {
        log.warn("유효하지 않은 값이 사용되었습니다. \n{}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("유효하지 않은 값이 사용되었습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception) {
        log.warn("요청된 데이터 형식이 잘못 되었습니다. \n{}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("요청된 데이터 형식이 잘못 되었습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleSQLException(final SQLException exception) {
        log.warn("SQL Exception이 발생하였습니다. \n{}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("유효하지 않은 데이터가 포함된 요청입니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleRuntimeException(final Exception exception) {
        log.error("예기치 못한 오류가 발생했습니다.", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("예기치 못한 오류가 발생했습니다.");
    }
}
