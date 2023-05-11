package subway.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

public class NotFoundLineException extends ApplicationException {

    private static final String MESSAGE = "%s 노선은 존재하지 않습니다.";
    private static final String MESSAGE_ID = "%d번 노선은 존재하지 않습니다.";

    public NotFoundLineException(final Long id) {
        super(String.format(MESSAGE_ID, id));
    }

    public NotFoundLineException(final String name) {
        super(String.format(MESSAGE, name));
    }

    @Override
    HttpStatus status() {
        return NOT_FOUND;
    }
}
