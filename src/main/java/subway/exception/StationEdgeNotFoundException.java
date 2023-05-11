package subway.exception;

public class StationEdgeNotFoundException extends RuntimeException{
    public StationEdgeNotFoundException() {
        super("구간을 찾을 수 없습니다.");
    }
}
