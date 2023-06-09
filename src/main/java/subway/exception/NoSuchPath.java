package subway.exception;

import org.springframework.http.HttpStatus;

public class NoSuchPath extends SubwayException {
    public NoSuchPath(ErrorCode errorcode, String sourceStationName, String targetStationName) {
        super(sourceStationName
                        + " - "
                        + targetStationName
                        + "은 "
                        + errorcode.getErrorMessage(),
                HttpStatus.BAD_REQUEST);
    }
}
