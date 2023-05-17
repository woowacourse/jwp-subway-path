package subway.advice;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.ExceptionResponse;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger;
    
    private ResponseEntity<ExceptionResponse> logAndRespond(final String message, final HttpStatus status) {
        logger.error(message);
        return ResponseEntity.status(status).body(new ExceptionResponse("[ERROR] " + message));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        return logAndRespond("서버가 응답할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException (final IllegalArgumentException exception) {
        return logAndRespond(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final String exceptionMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
        
        return logAndRespond(exceptionMessage, HttpStatus.BAD_REQUEST);
    }
}