package subway.ui.line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.application.line.port.in.LineNotFoundException;

@RestControllerAdvice
public class LineExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    private ResponseEntity<Void> handleLineNotFound(final LineNotFoundException exception) {
        log.warn("노선을 찾을 수 없습니다.", exception);
        return ResponseEntity.notFound().build();
    }
}
