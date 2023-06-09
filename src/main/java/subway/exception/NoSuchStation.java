package subway.exception;

import org.springframework.http.HttpStatus;

public class NoSuchStation extends SubwayException {
    public NoSuchStation(ErrorCode errorCode, String stationName) {
        super(stationName + "은 " + errorCode.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
}
