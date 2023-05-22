package subway.common.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.exception.BadRequestException;
import subway.common.exception.ServerErrorException;

import java.sql.SQLException;

@RestControllerAdvice
public class CommonControllerAdvice {
    private final Logger LOGGER = LoggerFactory.getLogger(CommonControllerAdvice.class.getName());

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<Void> handleServerErrorException(ServerErrorException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity
                .status(e.getHttpStatus())
                .build();
    }
}
