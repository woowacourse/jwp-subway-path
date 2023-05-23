package subway.exception.line;

import org.springframework.http.HttpStatus;
import subway.exception.common.BusinessException;

public class CanNotFoundLineException extends BusinessException {

  public CanNotFoundLineException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
