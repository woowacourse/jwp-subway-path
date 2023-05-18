package subway.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import subway.domain.exception.BusinessException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnhandled(Exception e) {
        logger.error("예기치 못한 오류: " + e);
        e.printStackTrace();

        return ResponseEntity.internalServerError()
                .body(ExceptionResponse.withoutMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadable() {
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.of("요청 페이로드를 읽지 못했습니다"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> exceptionMessages = exception.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(ExceptionResponse.of(exceptionMessages));
    }
}
