package subway.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

import org.springframework.http.HttpStatus;

public class DuplicateLineException extends ApplicationException {

    private static final String MESSAGE = "%s 노선은 이미 존재합니다.";

    public DuplicateLineException(final String name) {
        super(String.format(MESSAGE, name));
    }

    @Override
    HttpStatus status() {
        return CONFLICT;
    }
}
