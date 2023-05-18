package subway.exception.common;

public class NotFoundStationException extends NotFoundException {

    private static final String STATION_NOT_FOUND_MESSAGE = "해당 역이 존재하지 않습니다.";

    public NotFoundStationException() {
        super(STATION_NOT_FOUND_MESSAGE);
    }
}
