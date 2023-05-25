package subway.line.exception;

import org.springframework.http.HttpStatus;
import subway.exception.BusinessException;

public class CanNotDuplicatedLineNameException extends BusinessException {

  public CanNotDuplicatedLineNameException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
