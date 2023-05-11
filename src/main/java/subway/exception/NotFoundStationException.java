package subway.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

public class NotFoundStationException extends ApplicationException {

    private static final String MESSAGE = "%s 역은 존재하지 않는 역입니다.";

    public NotFoundStationException(final String name) {
        super(String.format(MESSAGE, name));
    }

    @Override
    public HttpStatus status() {
        return NOT_FOUND;
    }
}
