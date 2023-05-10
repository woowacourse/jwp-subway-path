package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.ui.dto.response.ExceptionResponse;

import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionResponse> handleSQLException(SQLException ex) {
        logger.error("SQLException : ", ex);

        return ResponseEntity.badRequest().body(new ExceptionResponse("중복된 이름이 존재합니다."));
    }
}
