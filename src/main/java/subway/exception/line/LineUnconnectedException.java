package subway.exception.line;

public class LineUnconnectedException extends LineException {
    public LineUnconnectedException() {
        super("연결되지 않은 역이 있습니다");
    }

}
