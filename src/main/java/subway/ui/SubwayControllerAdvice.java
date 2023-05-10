package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import subway.exception.DomainException;
import subway.exception.ExceptionType;

@ControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionType> handle(DomainException domainException) {
        return ResponseEntity.badRequest().body(domainException.getExceptionType());
    }
}
