package subway.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException{

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }


    public abstract HttpStatus status();

}
