package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.application.exception.AddSectionException;
import subway.application.exception.NoSuchStationException;
import subway.dto.ErrorResponseDto;

import java.sql.SQLException;

@ControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> validHandler(final MethodArgumentNotValidException exception) {
        final String message = exception.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponseDto(message));
    }

    @ExceptionHandler({SQLException.class,IllegalArgumentException.class,AddSectionException.class,NoSuchStationException.class})
    public ResponseEntity<ErrorResponseDto> handleSQLException(final Exception exception) {
        final String message = exception.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponseDto(message));
    }
}
