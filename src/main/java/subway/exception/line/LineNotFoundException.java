package subway.exception.line;

public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException() {
        super("해당 되는 노선을 찾을 수 없습니다.");
    }
}
