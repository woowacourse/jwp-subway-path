package subway.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.DatabaseException;
import subway.exception.GlobalException;
import subway.presentation.dto.response.BadResponse;

import java.net.BindException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BadResponse> handleGlobalException(GlobalException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BadResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<BadResponse> handleDatabaseException(DatabaseException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new BadResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<BadResponse> handleBindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BadResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<BadResponse> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BadResponse("예기치 못한 오류가 발생하였습니다."));
    }
}
