package subway.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

import org.springframework.http.HttpStatus;

public class DuplicateStationException extends ApplicationException {

    private static final String MESSAGE = "%s 역은 이미 존재합니다.";

    public DuplicateStationException(final String name) {
        super(String.format(MESSAGE, name));
    }


    @Override
    public HttpStatus status() {
        return CONFLICT;
    }
}
