package subway.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.application.core.exception.ExpectedException;
import subway.presentation.dto.ExceptionResponse;

import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({DataAccessException.class, SQLException.class})
    public ResponseEntity<ExceptionResponse> dataException(DataAccessException e) {
        logger.warn(e.getMessage(), e);

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse("유효하지 않은 입력입니다."));
    }

    @ExceptionHandler(ExpectedException.class)
    public ResponseEntity<ExceptionResponse> expectedException(ExpectedException e) {
        logger.warn(e.getMessage(), e);

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> unexpectedException(Exception e) {
        logger.error(e.getMessage(), e);

        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse("예기치 못한 오류가 발생했습니다."));
    }
}
