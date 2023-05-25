package subway.line.exception;

import org.springframework.http.HttpStatus;
import subway.exception.BusinessException;

public class CanNotDuplicatedSectionException extends BusinessException {

  public CanNotDuplicatedSectionException(final String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
