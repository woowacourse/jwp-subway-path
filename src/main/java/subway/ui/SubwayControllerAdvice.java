package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.application.exception.AddSectionException;
import subway.dto.ErrorResponseDto;

import java.sql.SQLException;

@ControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> validHandler(final MethodArgumentNotValidException exception) {
        final String message = exception.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponseDto(message));
    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponseDto> handleSQLException(final SQLException exception) {
        final String message = exception.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponseDto(message));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(final IllegalArgumentException exception) {
        final String message = exception.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponseDto(message));
    }
    @ExceptionHandler(AddSectionException.class)
    public ResponseEntity<ErrorResponseDto> handleAddSectionException(final AddSectionException exception) {
        final String message = exception.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponseDto(message));
    }
}
