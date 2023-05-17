package subway.exception;

import org.springframework.http.HttpStatus;

public class DistanceValueValidateException extends CustomException {

    public DistanceValueValidateException() {
        super("거리는 1이상의 정수이어야 합니다.", HttpStatus.BAD_REQUEST);
    }
}
