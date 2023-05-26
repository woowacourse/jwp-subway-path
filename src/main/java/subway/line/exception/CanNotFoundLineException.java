package subway.line.exception;

import org.springframework.http.HttpStatus;
import subway.exception.BusinessException;

public class CanNotFoundLineException extends BusinessException {

  public CanNotFoundLineException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
