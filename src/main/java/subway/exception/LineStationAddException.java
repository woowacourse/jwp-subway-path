package subway.exception;

import org.springframework.http.HttpStatus;

public class LineStationAddException extends ApplicationException {

    public LineStationAddException(String message) {
        super(message);
    }

    @Override
    HttpStatus status() {
        return HttpStatus.CONFLICT;
    }
}
