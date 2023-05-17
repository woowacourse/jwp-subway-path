package subway.ui;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import subway.domain.exception.DomainException;
import subway.domain.exception.ExceptionType;

@ControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionType> handle(DomainException domainException) {
        return ResponseEntity.badRequest().body(domainException.getExceptionType());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
