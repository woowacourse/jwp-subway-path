package subway.exception.subway;

public class PathNotFoundException extends SubwayException {
    private static final String MESSAGE = "경로를 찾을 수 없습니다.";

    public PathNotFoundException() {
        super(MESSAGE);
    }
}
