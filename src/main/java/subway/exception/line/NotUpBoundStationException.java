package subway.exception.line;

public class NotUpBoundStationException extends LineException {

    private final static String NOT_UP_BOUND_STATION_MESSAGE = "올바른 상행종점이 아닙니다";

    public NotUpBoundStationException() {
        super(NOT_UP_BOUND_STATION_MESSAGE);
    }
}
