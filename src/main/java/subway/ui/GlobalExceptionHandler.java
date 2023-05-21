package subway.ui;

import java.util.NoSuchElementException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class,
        DuplicateKeyException.class, NoSuchElementException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleRequestException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("[ERROR] " + exception.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorResponse> handleNotHandledDBException(
        EmptyResultDataAccessException exception) {
        ErrorResponse errorResponse = new ErrorResponse("DB 조회에 실패하였습니다. 처리되지 않은 예외이니 관리자에게 문의하세요.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleNotHandledException(
        Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("처리되지 않은 예외입니다. 관리자에게 문의하세요.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
