package subway.exception;

import org.springframework.http.HttpStatus;

public class IllegalAgeException extends ApplicationException {

    private static final String message = "잘못된 나이입니다.";

    public IllegalAgeException() {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
