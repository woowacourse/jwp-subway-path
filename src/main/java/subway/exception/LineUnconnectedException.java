package subway.exception;

public class LineUnconnectedException extends SubwayException {
    public LineUnconnectedException() {
        super("연결되지 않은 역이 있습니다");
    }

}
