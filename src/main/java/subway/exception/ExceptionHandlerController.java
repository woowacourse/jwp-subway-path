package subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ExceptionResponse> handleDomainException(
      IllegalArgumentException exception) {
    errorLogging(exception);

    return ResponseEntity.badRequest()
        .body(new ExceptionResponse(exception.getMessage()));
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException exception) {
    errorLogging(exception);

    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()),
        exception.getHttpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleUnexpectedException(Exception exception) {
    errorLogging(exception);

    return ResponseEntity.internalServerError()
        .body(new ExceptionResponse(String.format("전화 주세요 : %s", exception.getMessage())));
  }

  @ExceptionHandler(DataAccessResourceFailureException.class)
  public ResponseEntity<ExceptionResponse> handleDatabaseException(
      DataAccessResourceFailureException exception) {
    errorLogging(exception);

    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private void errorLogging(Exception exception) {
    log.info(
        "클래스 이름 = {} 메시지 = {}",
        exception.getClass().getSimpleName(),
        exception.getMessage()
    );
  }
}
