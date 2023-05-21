package subway.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.*;

@RestControllerAdvice
public class ControllerAdvice {

    public static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler({CannotLinkException.class, DuplicateSectionException.class, DuplicateSectionException.class,
            IllegalDistanceException.class, IllegalLineNameException.class, IllegalStationNameException.class,
            LineNotFoundException.class, StationNotFoundException.class})
    public ResponseEntity<String> handleIllegalArgumentException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
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
