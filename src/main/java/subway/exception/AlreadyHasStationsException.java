package subway.exception;

import org.springframework.http.HttpStatus;

public class AlreadyHasStationsException extends CustomException {

    public AlreadyHasStationsException() {
        super("이미 등록되어 있는 역들입니다.", HttpStatus.BAD_REQUEST);
    }
}
