package subway.exception;

import org.springframework.http.HttpStatus;

public class StationNotFoundException extends CustomException {

    public StationNotFoundException() {
        super("해당 역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
