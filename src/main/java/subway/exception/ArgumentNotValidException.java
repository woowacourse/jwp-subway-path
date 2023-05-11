package subway.exception;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.springframework.http.HttpStatus;

public class ArgumentNotValidException extends ApplicationException{

    public ArgumentNotValidException(String message) {
        super(message);
    }

    @Override
    HttpStatus status() {
        return UNPROCESSABLE_ENTITY;
    }
}
