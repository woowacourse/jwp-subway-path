package subway.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.SubwayException;

import java.sql.SQLException;

@RestControllerAdvice
public class ControllerAdvice {

    public static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<String> handleSubwayException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSqlException(Exception e) {
        logError(e);
        return ResponseEntity.badRequest()
                .body("SQL 오류가 발생했습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logError(e);
        return ResponseEntity.badRequest().body("알 수 없는 오류가 발생했습니다.");
    }


    private void logError(Throwable throwable) {
        String exceptionName = throwable.getClass().toString();
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if (stackTraceElements.length > 0) {
            StackTraceElement stackTraceElement = stackTraceElements[0];
            String errorLocation = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
            logger.error("{} at {}: {}", exceptionName, errorLocation, throwable.getMessage());
        } else {
            logger.error("{}: {}", exceptionName, throwable.getMessage());
        }
    }
}
