package subway.exception;

public class LineOrStationNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "노선이나 역을 찾을 수 없습니다.";

    public LineOrStationNotFoundException() {
        super(MESSAGE);
    }
}
