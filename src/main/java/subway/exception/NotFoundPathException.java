package subway.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

public class NotFoundPathException extends ApplicationException {

    private static final String MESSAGE = "두 역은 연결되지않았습니다.";

    public NotFoundPathException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus status() {
        return NOT_FOUND;
    }
}
