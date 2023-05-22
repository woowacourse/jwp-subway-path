package subway.exception.line;

public class NotDownBoundStationException extends LineException {

    private final static String NOT_DOWN_BOUND_STATION_MESSAGE = "올바른 하행 종점이 아닙니다.";

    public NotDownBoundStationException() {
        super(NOT_DOWN_BOUND_STATION_MESSAGE);
    }
}
