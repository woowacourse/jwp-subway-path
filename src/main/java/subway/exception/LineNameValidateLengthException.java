package subway.exception;

import org.springframework.http.HttpStatus;

public class LineNameValidateLengthException extends CustomException {

    public LineNameValidateLengthException() {
        super("노선의 이름은 20자가 넘을 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
